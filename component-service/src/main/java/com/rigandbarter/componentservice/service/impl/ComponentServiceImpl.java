package com.rigandbarter.componentservice.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rigandbarter.componentservice.dto.*;
import com.rigandbarter.componentservice.mapper.ComponentMapper;
import com.rigandbarter.componentservice.repository.file.IFileRepository;
import com.rigandbarter.componentservice.service.IComponentService;
import com.rigandbarter.componentservice.model.*;
import com.rigandbarter.componentservice.repository.document.IComponentRepository;
import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.core.models.ComponentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.rigandbarter.core.models.ComponentCategory.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComponentServiceImpl implements IComponentService {

    private final IComponentRepository componentRepository;
    private final IFileRepository fileRepository;

    private static final String[] DATA_FILE_NAMES = {
            "case.csv", "motherboard.csv", "cpu.csv",
            "gpu.csv", "memory.csv", "power-supply.csv",
            "hard-drive.csv", "solid-state-drive.csv"
    };

    @Override
    public ComponentResponse createComponent(CreateComponentRequest componentRequest, MultipartFile image) {
        log.info("Uploading image to file storage");
        var fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        var key = UUID.randomUUID() + "." + fileExtension;
        var imageUrl = fileRepository.uploadFile(key, image);

        log.info("All images successfully uploaded to file storage");

        String id = UUID.randomUUID().toString();
        Component component = componentRepository.saveComponent(requestToComponent(componentRequest, id, imageUrl));

        log.info("Successfully created component: " + component.getId());
        return componentToResponse(component);
    }

    @Override
    public List<ComponentResponse> saveAllComponents(MultipartFile zipDataFile) {

        // Convert the zip data to components
        List<Component> componentsToAdd = new ArrayList<>();
        try {
            List<String> files = createFilesFromZip(zipDataFile);
            for (String file : files) {
                componentsToAdd.addAll(extractComponentPojosFromFile(file));
            }

            // Delete all files after they have been processed
            for (String file : DATA_FILE_NAMES) {
                File f = new File(file);
                if (f.exists())
                    f.delete();
            }
        }
        catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }

        // Remove components that already exist in the database
        List<Component> currentComponents = componentRepository.getAllComponents().stream()
                                                .peek(c -> c.setId(null))
                                                .toList();
        componentsToAdd.removeAll(currentComponents);

        // Save the unique components to the database
        List<Component> savedComponents = componentRepository.saveAllComponents(componentsToAdd);

        return savedComponents.stream()
                .map(this::componentToResponse)
                .toList();
    }

    @Override
    public List<ComponentResponse> getAllComponents() {
        return componentRepository.getAllComponents().stream()
                .map(this::componentToResponse)
                .toList();
    }

    @Override
    public List<ComponentResponse> getAllComponentsOfCategory(ComponentCategory category) {
        return componentRepository.getAllComponentsOfCategory(category).stream()
                .map(this::componentToResponse)
                .toList();
    }

    @Override
    public PagedComponentResponse getPaginatedComponentsOfCategory(ComponentCategory category,
                                                                   int page,
                                                                   int numPerPage,
                                                                   String sortColumn,
                                                                   boolean descending,
                                                                   String searchTerm) {

        List<ComponentResponse> components = componentRepository.getPaginatedComponentsOfCategory(category, page, numPerPage, sortColumn, descending, searchTerm)
                .stream()
                .map(this::componentToResponse)
                .toList();

        int totalItems = componentRepository.getPaginatedComponentsOfCategorySize(category, searchTerm);

        return PagedComponentResponse.builder()
                .numItems(totalItems)
                .components(components)
                .build();
    }

    /**
     * Creates a list of Component pojos from the passed in csv file
     * @param fileName The csv file to parse for objects
     * @return The list of components created from file
     */
    private List<Component> extractComponentPojosFromFile(String fileName) throws FileNotFoundException {
        log.info("Extracting file: " + fileName);
        Reader reader = new BufferedReader(new FileReader(fileName));
        Class classToUse = switch (fileName) {
            case "case.csv" -> CaseComponent.class;
            case "motherboard.csv" -> MotherboardComponent.class;
            case "cpu.csv" -> ProcessorComponent.class;
            case "gpu.csv" -> VideoCardComponent.class;
            case "memory.csv" -> MemoryComponent.class;
            case "power-supply.csv" -> PowerSupplyComponent.class;
            case "hard-drive.csv" -> HardDriveComponent.class;
            case "solid-state-drive.csv" -> SolidStateDriveComponent.class;
            default -> throw new RuntimeException("Failed to find class for file: " + fileName);
        };

        CsvToBean csvReader = new CsvToBeanBuilder<Component>(reader)
                .withType(classToUse)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();

        log.info("Parsing csv file to class");
        return csvReader.parse();
    }

    /**
     * Creates files for each entry in the zip file
     * @param zipDataFile The zip containing the files
     * @return A list of file names (as strings) that were created
     * @throws IOException If fails to read file
     */
    private List<String> createFilesFromZip(MultipartFile zipDataFile) throws IOException {
        log.info("Creating files from zip data: " + zipDataFile.getName());
        log.info("Creating zip stream. Current directory: " + System.getProperty("user.dir"));

        String currentDir2 = Paths.get("").toAbsolutePath().normalize().toString();
        System.out.println("Current working directory (Method 2): " + currentDir2);

        List<String> files = new ArrayList<>();
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(zipDataFile.getBytes()));
        ZipEntry entry;
        String tempDir = System.getProperty("java.io.tmpdir");
        log.info("Temp directory: " + tempDir);
        while ((entry = zipStream.getNextEntry()) != null) {

            String entryName = entry.getName();
            log.info("Creating file: " + entryName);

            // Create file attributes
            FileAttribute<?>[] attrs = new FileAttribute<?>[0];
            File file = Files.createTempFile(Path.of(tempDir), entryName, ".csv", attrs).toFile();

            log.info("File created to write out to: " + file.getAbsolutePath());

            FileOutputStream out = new FileOutputStream(file);
            log.info("successfully created file");

            byte[] byteBuff = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = zipStream.read(byteBuff)) != -1)
            {
                out.write(byteBuff, 0, bytesRead);
            }

            out.close();
            zipStream.closeEntry();

            files.add(entryName);
            log.info("Successfully wrote file: " + entryName);
        }
        zipStream.close();

        return files;
    }

    /**
     * Converts a CreateComponentRequest to a Component
     * @param request The request to convert
     * @param id The id to associate with the component
     * @param imageUrl The image url to associate with the component
     * @return The converted component
     */
    private Component requestToComponent(CreateComponentRequest request, String id, String imageUrl) {
        Component res = switch (request.getComponentCategory()) {
            case HARD_DRIVE -> ComponentMapper.dtoToEntity((CreateHardDriveComponentRequest) request, id, imageUrl);
            case SOLID_STATE_DRIVE -> ComponentMapper.dtoToEntity((CreateSolidStateDriveComponentRequest) request, id, imageUrl);
            case MOTHERBOARD -> ComponentMapper.dtoToEntity((CreateMotherboardComponentRequest) request, id, imageUrl);
            case MEMORY -> ComponentMapper.dtoToEntity((CreateMemoryComponentRequest) request, id, imageUrl);
            case CPU -> ComponentMapper.dtoToEntity((CreateProcessorComponentRequest) request, id, imageUrl);
            case GPU -> ComponentMapper.dtoToEntity((CreateVideoCardComponentRequest) request, id, imageUrl);
            case POWER_SUPPLY -> ComponentMapper.dtoToEntity((CreatePowerSupplyComponentRequest) request, id, imageUrl);
            case CASE -> ComponentMapper.dtoToEntity((CreateCaseComponentRequest) request, id, imageUrl);
        };

        if (res == null)
            log.error("Failed to convert component request: " + id);

        return res;
    }

    /**
     * Converts a Component to a ComponentResponse
     * @param component The component to convert
     * @return The converted component
     */
    private ComponentResponse componentToResponse(Component component) {
        ComponentResponse res = switch (component.getCategory()) {
            case HARD_DRIVE -> ComponentMapper.entityToDto((HardDriveComponent) component);
            case SOLID_STATE_DRIVE -> ComponentMapper.entityToDto((SolidStateDriveComponent) component);
            case MOTHERBOARD -> ComponentMapper.entityToDto((MotherboardComponent) component);
            case MEMORY -> ComponentMapper.entityToDto((MemoryComponent) component);
            case CPU -> ComponentMapper.entityToDto((ProcessorComponent) component);
            case GPU -> ComponentMapper.entityToDto((VideoCardComponent) component);
            case POWER_SUPPLY -> ComponentMapper.entityToDto((PowerSupplyComponent) component);
            case CASE -> ComponentMapper.entityToDto((CaseComponent) component);
        };

        if(res == null)
            log.error("Failed to convert component: " + component.getId());

        return res;
    }
}
