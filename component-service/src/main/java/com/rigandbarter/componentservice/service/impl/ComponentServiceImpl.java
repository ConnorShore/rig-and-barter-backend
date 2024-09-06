package com.rigandbarter.componentservice.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rigandbarter.componentservice.mapper.ComponentMapper;
import com.rigandbarter.componentservice.service.IComponentService;
import com.rigandbarter.componentservice.model.*;
import com.rigandbarter.componentservice.repository.document.IComponentRepository;
import com.rigandbarter.core.models.ComponentCategory;
import com.rigandbarter.core.models.ComponentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComponentServiceImpl implements IComponentService {

    private final IComponentRepository componentRepository;

    @Override
    public List<ComponentResponse> saveAllComponents(MultipartFile zipDataFile) {

        // Convert the zip data to components
        List<Component> componentsToAdd = new ArrayList<>();
        try {
            List<String> files = createFilesFromZip(zipDataFile);
            for (String file : files) {
                componentsToAdd.addAll(extractComponentPojosFromFile(file));
            }
            // TODO: Delete all created files
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

    /**
     * Creates a list of Component pojos from the passed in csv file
     * @param fileName The csv file to parse for objects
     * @return The list of components created from file
     */
    private List<Component> extractComponentPojosFromFile(String fileName) throws FileNotFoundException {
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

        return csvReader.parse();
    }

    /**
     * Creates files for each entry in the zip file
     * @param zipDataFile The zip containing the files
     * @return A list of file names (as strings) that were created
     * @throws IOException If fails to read file
     */
    private List<String> createFilesFromZip(MultipartFile zipDataFile) throws IOException {
        List<String> files = new ArrayList<>();
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(zipDataFile.getBytes()));
        ZipEntry entry;
        while ((entry = zipStream.getNextEntry()) != null) {

            String entryName = entry.getName();

            FileOutputStream out = new FileOutputStream(entryName);

            byte[] byteBuff = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = zipStream.read(byteBuff)) != -1)
            {
                out.write(byteBuff, 0, bytesRead);
            }

            out.close();
            zipStream.closeEntry();

            files.add(entryName);
        }
        zipStream.close();

        return files;
    }

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
