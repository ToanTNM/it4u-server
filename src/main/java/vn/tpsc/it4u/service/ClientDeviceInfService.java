package vn.tpsc.it4u.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antlr.StringUtils;
import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.model.ClientDeviceInf;
import vn.tpsc.it4u.model.Contract;
import vn.tpsc.it4u.model.SitesName;
import vn.tpsc.it4u.payload.ClientDeviceInfSummary;
import vn.tpsc.it4u.repository.ClientDeviceInfRepository;
import vn.tpsc.it4u.repository.ContractRepository;
import vn.tpsc.it4u.repository.SitesNameRepository;

@Service
@ExtensionMethod({StringUtils.class})
public class ClientDeviceInfService {
    @Autowired
    private ClientDeviceInfRepository clientDeviceInfRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private SitesNameRepository sitesNameRepository;

    public boolean createClientDeviceInf(JSONObject data) {
        ClientDeviceInf clientDeviceInf = new ClientDeviceInf(
            data.getString("backup"),
            data.getString("maintenance"),
            data.getString("ispWan1"),
            data.getString("modelWan1"),
            data.getString("ipWan1"),
            data.getString("passWan1"),
            data.getString("ispWan2"),
            data.getString("modelWan2"),
            data.getString("ipWan2"),
            data.getString("passWan2"),
            data.getString("ispWan3"),
            data.getString("modelWan3"),
            data.getString("ipWan3"),
            data.getString("passWan3"),
            data.getString("ispWan4"),
            data.getString("modelWan4"),
            data.getString("ipWan4"),
            data.getString("passWan4"),
            data.getString("modelLb"),
            data.getString("ipLb"),
            data.getString("passLb"),
            data.getString("modelOther"),
            data.getString("ipOther"),
            data.getString("passOther"),
            data.getString("reboot"),
            data.getString("monitor"),
            data.getString("method"),
            data.getString("location"),
            data.getString("openPort"),
            data.getString("firmware"),
            data.getString("wanGroup"),
            data.getString("note")
        );
        try {
            Contract contract = contractRepository.findByClientName(data.getString("clientName"));
            clientDeviceInf.setContract(contract);
        } catch (Exception e) {
        }
        try {
            if (sitesNameRepository.existsBySitename(data.getString("siteName"))) {
                SitesName sitesName = sitesNameRepository.findBySitename(data.getString("siteName"));
                clientDeviceInf.setSitename(sitesName);
            }
            else {
                SitesName sitesName = new SitesName(
                    data.getString("siteName"),
                    data.getString("idName")
                );
                sitesNameRepository.save(sitesName);
                clientDeviceInf.setSitename(sitesName);
            }
            
        } catch (Exception e) {
        }
        clientDeviceInfRepository.save(clientDeviceInf);
        return true;
    }

    public Boolean uploadClientDeviceInf(JSONObject data, JSONObject infoContract) {
        ClientDeviceInf clientDeviceInf = new ClientDeviceInf(
            data.getString("backup"),
            data.getString("maintenance"),
            data.getString("ispWan1"),
            data.getString("modelWan1"),
            data.getString("ipWan1"),
            data.getString("passWan1"),
            data.getString("ispWan2"),
            data.getString("modelWan2"),
            data.getString("ipWan2"),
            data.getString("passWan2"),
            data.getString("ispWan3"),
            data.getString("modelWan3"),
            data.getString("ipWan3"),
            data.getString("passWan3"),
            data.getString("ispWan4"),
            data.getString("modelWan4"),
            data.getString("ipWan4"),
            data.getString("passWan4"),
            data.getString("modelLb"),
            data.getString("ipLb"),
            data.getString("passLb"),
            data.getString("modelOther"),
            data.getString("ipOther"),
            data.getString("passOther"),
            data.getString("reboot"),
            data.getString("monitor"),
            data.getString("method"),
            data.getString("location"),
            data.getString("openPort"),
            data.getString("firmware"),
            data.getString("wanGroup"),
            data.getString("note")
        );
        try {
            if (contractRepository.existsByParamContract(infoContract.getString("customId"), infoContract.getString("companyName"), 
                infoContract.getString("street"), infoContract.getString("contracts"))) {
                Contract contract = contractRepository.findByParamContract(infoContract.getString("customId"), infoContract.getString("companyName"), 
                infoContract.getString("street"), infoContract.getString("contracts"));
                contract.setPhone(infoContract.getString("phone"));
                contractRepository.save(contract);
                clientDeviceInf.setContract(contract);
            }
            else {
                Contract contract = new Contract(
                    infoContract.getString("customId"), 
                    infoContract.getString("contracts"),
                    infoContract.getString("companyName"), 
                    infoContract.getString("servicePlan"),
                    infoContract.getString("street"),
                    infoContract.getString("phone")
                );
                contractRepository.save(contract);
                clientDeviceInf.setContract(contract);
            }
        } catch (Exception e) {
        }
        try {
            if (sitesNameRepository.existsBySitename(data.getString("siteName"))) {
                SitesName sitesName = sitesNameRepository.findBySitename(data.getString("siteName"));
                clientDeviceInf.setSitename(sitesName);
            }
            else {
                String idName = data.getString("siteName");
                try {
                    if (data.getString("idName").equals(""))
                        idName = data.getString("siteName");
                    else 
                        idName = data.getString("idName");
                } catch (Exception e) {
                }
                SitesName sitesName = new SitesName(
                    data.getString("siteName"),
                    idName
                );
                sitesNameRepository.save(sitesName);
                clientDeviceInf.setSitename(sitesName);
            }
            
        } catch (Exception e) {
        }
        clientDeviceInfRepository.save(clientDeviceInf);
        return true;
    }

    public Boolean updateClientDeviceInf(long id, JSONObject data) {
        clientDeviceInfRepository.findById(id);
        ClientDeviceInf clientDeviceInf = clientDeviceInfRepository.findById(id);
        clientDeviceInf.setBackup(data.getString("backup"));
        clientDeviceInf.setMaintenance(data.getString("maintenance"));
        clientDeviceInf.setIspWan1(data.getString("ispWan1"));
        clientDeviceInf.setModelWan1(data.getString("modelWan1"));
        clientDeviceInf.setIpWan1(data.getString("ipWan1"));
        clientDeviceInf.setPassWan1(data.getString("passWan1"));
        clientDeviceInf.setIspWan2(data.getString("ispWan2"));
        clientDeviceInf.setModelWan2(data.getString("modelWan2"));
        clientDeviceInf.setIpWan2(data.getString("ipWan2"));
        clientDeviceInf.setPassWan2(data.getString("passWan2"));
        clientDeviceInf.setIspWan3(data.getString("ispWan3"));
        clientDeviceInf.setModelWan3(data.getString("modelWan3"));
        clientDeviceInf.setIpWan3(data.getString("ipWan3"));
        clientDeviceInf.setPassWan3(data.getString("passWan3"));
        clientDeviceInf.setIspWan4(data.getString("ispWan4"));
        clientDeviceInf.setModelWan4(data.getString("modelWan4"));
        clientDeviceInf.setIpWan4(data.getString("ipWan4"));
        clientDeviceInf.setPassWan4(data.getString("passWan4"));
        clientDeviceInf.setModelLb(data.getString("modelLb"));
        clientDeviceInf.setIpLb(data.getString("ipLb"));
        clientDeviceInf.setPassLb(data.getString("passLb"));
        clientDeviceInf.setModelOther(data.getString("modelOther"));
        clientDeviceInf.setIpOther(data.getString("ipOther"));
        clientDeviceInf.setPassOther(data.getString("passOther"));
        clientDeviceInf.setReboot(data.getString("reboot"));
        clientDeviceInf.setMonitor(data.getString("monitor"));
        clientDeviceInf.setMethod(data.getString("method"));
        clientDeviceInf.setLocation(data.getString("location"));
        clientDeviceInf.setOpenPort(data.getString("openPort"));
        clientDeviceInf.setFirmware(data.getString("firmware"));
        clientDeviceInf.setWanGroup(data.getString("wanGroup"));
        clientDeviceInf.setNote(data.getString("note"));
        Contract contract = contractRepository.findByClientName(data.getString("clientName"));
        clientDeviceInf.setContract(contract);
        SitesName sitesName = sitesNameRepository.findByIdname(data.getString("idName"));
        clientDeviceInf.setSitename(sitesName);
        clientDeviceInfRepository.save(clientDeviceInf);
        return true;
    }

    public List<ClientDeviceInfSummary> findLimitClientDeviceInf(Long num) {
        List<ClientDeviceInf> clientDeviceInfs = clientDeviceInfRepository.findLimitByNum(num);
        List<ClientDeviceInfSummary> listClientDeviceInfs = clientDeviceInfs.stream()
            .map(clientDeviceInf -> new ClientDeviceInfSummary(
                clientDeviceInf.getId(),
                clientDeviceInf.getContract(),
                clientDeviceInf.getSitename(),
                clientDeviceInf.getBackup(),
                clientDeviceInf.getMaintenance(),
                clientDeviceInf.getIspWan1(),
                clientDeviceInf.getModelWan1(),
                clientDeviceInf.getIpWan1(),
                clientDeviceInf.getPassWan1(),
                clientDeviceInf.getIspWan2(),
                clientDeviceInf.getModelWan2(),
                clientDeviceInf.getIpWan2(),
                clientDeviceInf.getPassWan2(),
                clientDeviceInf.getIspWan3(),
                clientDeviceInf.getModelWan3(),
                clientDeviceInf.getIpWan3(),
                clientDeviceInf.getPassWan3(),
                clientDeviceInf.getIspWan4(),
                clientDeviceInf.getModelWan4(),
                clientDeviceInf.getIpWan4(),
                clientDeviceInf.getPassWan4(),
                clientDeviceInf.getModelLb(),
                clientDeviceInf.getIpLb(),
                clientDeviceInf.getPassLb(),
                clientDeviceInf.getModelOther(),
                clientDeviceInf.getIpOther(),
                clientDeviceInf.getPassOther(),
                clientDeviceInf.getReboot(),
                clientDeviceInf.getMonitor(),
                clientDeviceInf.getMethod(),
                clientDeviceInf.getLocation(),
                clientDeviceInf.getOpenPort(),
                clientDeviceInf.getFirmware(),
                clientDeviceInf.getWanGroup(),
                clientDeviceInf.getNote()
            )).collect(Collectors.toList());
        return listClientDeviceInfs;
    }

    public List<ClientDeviceInfSummary> findAllClientDeviceInf() {
        List<ClientDeviceInf> clientDeviceInfs = clientDeviceInfRepository.findAll();
        List<ClientDeviceInfSummary> listClientDeviceInfs = clientDeviceInfs.stream()
            .map(clientDeviceInf -> new ClientDeviceInfSummary(
                clientDeviceInf.getId(),
                clientDeviceInf.getContract(),
                clientDeviceInf.getSitename(),
                clientDeviceInf.getBackup(),
                clientDeviceInf.getMaintenance(),
                clientDeviceInf.getIspWan1(),
                clientDeviceInf.getModelWan1(),
                clientDeviceInf.getIpWan1(),
                clientDeviceInf.getPassWan1(),
                clientDeviceInf.getIspWan2(),
                clientDeviceInf.getModelWan2(),
                clientDeviceInf.getIpWan2(),
                clientDeviceInf.getPassWan2(),
                clientDeviceInf.getIspWan3(),
                clientDeviceInf.getModelWan3(),
                clientDeviceInf.getIpWan3(),
                clientDeviceInf.getPassWan3(),
                clientDeviceInf.getIspWan4(),
                clientDeviceInf.getModelWan4(),
                clientDeviceInf.getIpWan4(),
                clientDeviceInf.getPassWan4(),
                clientDeviceInf.getModelLb(),
                clientDeviceInf.getIpLb(),
                clientDeviceInf.getPassLb(),
                clientDeviceInf.getModelOther(),
                clientDeviceInf.getIpOther(),
                clientDeviceInf.getPassOther(),
                clientDeviceInf.getReboot(),
                clientDeviceInf.getMonitor(),
                clientDeviceInf.getMethod(),
                clientDeviceInf.getLocation(),
                clientDeviceInf.getOpenPort(),
                clientDeviceInf.getFirmware(),
                clientDeviceInf.getWanGroup(),
                clientDeviceInf.getNote()
            )).collect(Collectors.toList());
        return listClientDeviceInfs;
    }

    public List<ClientDeviceInf> findAllByParam(String param) {
        List<ClientDeviceInf> clientDeviceInfs = clientDeviceInfRepository.findAllByParam(param);
        return clientDeviceInfs;
    }

    public Boolean deleteClientDeviceInf(Long id) {
        clientDeviceInfRepository.deleteById(id);
        return true;
    }
}
