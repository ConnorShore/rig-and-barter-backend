package com.rigandbarter.componentservice.controller.service.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rigandbarter.componentservice.controller.mapper.ComponentMapper;
import com.rigandbarter.componentservice.controller.service.IComponentService;
import com.rigandbarter.componentservice.dto.*;
import com.rigandbarter.componentservice.model.*;
import com.rigandbarter.componentservice.repository.document.IComponentRepository;
import com.rigandbarter.core.models.ComponentCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComponentServiceImpl implements IComponentService {

    private final IComponentRepository componentRepository;

    @Override
    public List<ComponentResponse> saveAllComponents(MultipartFile zipDataFile) {
        List<String> files = new ArrayList<>();
        List<Component> components = new ArrayList<>();
        try {
            ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(zipDataFile.getBytes()));
            ZipEntry entry = null;
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
        }
        catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }

        try {
            for(String file : files) {
                Reader reader = new BufferedReader(new FileReader(file));
                Class classToUse;
                switch(file) {
                    case "case.csv":
                        classToUse = CaseComponent.class;
                        break;
                    case "motherboard.csv":
                        classToUse = MotherboardComponent.class;
                        break;
                    case "cpu.csv":
                        classToUse = ProcessorComponent.class;
                        break;
                    case "gpu.csv":
                        classToUse = VideoCardComponent.class;
                        break;
                    case "memory.csv":
                        classToUse = MemoryComponent.class;
                        break;
                    case "power-supply.csv":
                        classToUse = PowerSupplyComponent.class;
                        break;
                    case "hard-drive.csv":
                        classToUse = HardDriveComponent.class;
                        break;
                    case "solid-state-drive.csv":
                        classToUse = SolidStateDriveComponent.class;
                        break;
                    default:
                        log.error("Cannot convert file to objects: " + file);
                        return null;

                }
                CsvToBean<Component> csvReader = new CsvToBeanBuilder(reader)
                        .withType(classToUse)
                        .withSeparator(',')
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreEmptyLine(true)
                        .build();

                List<Component> results = csvReader.parse();
                components.addAll(results);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }

        List<Component> currentComponents = componentRepository.getAllComponents().stream()
                                                .peek(c -> c.setId(null))
                                                .toList();
        components.removeAll(currentComponents);

        List<Component> savedComponents = componentRepository.saveAllComponents(components);

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

    public ComponentResponse componentToResponse(Component compoennt) {
        switch (compoennt.getCategory()) {
            case HARD_DRIVE -> ComponentMapper.entityToDto((HardDriveComponent) compoennt);
            case SOLID_STATE_DRIVE -> ComponentMapper.entityToDto((SolidStateDriveComponent) compoennt);
            case MOTHERBOARD -> ComponentMapper.entityToDto((MotherboardComponent) compoennt);
            case MEMORY -> ComponentMapper.entityToDto((MemoryComponent) compoennt);
            case CPU -> ComponentMapper.entityToDto((ProcessorComponent) compoennt);
            case GPU -> ComponentMapper.entityToDto((VideoCardComponent) compoennt);
            case POWER_SUPPLY -> ComponentMapper.entityToDto((PowerSupplyComponent) compoennt);
            case CASE -> ComponentMapper.entityToDto((CaseComponent) compoennt);
            default -> log.error("FAiled to convert component: " + compoennt.getId());
        }

        return null;
    }
}
