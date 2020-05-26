package vn.tpsc.it4u.service;

import vn.tpsc.it4u.util.StringUtils;
import org.springframework.stereotype.Service;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Optional;

import vn.tpsc.it4u.model.Reporter;
import vn.tpsc.it4u.payload.ReporterSummary;
import vn.tpsc.it4u.repository.ReporterRepository;
import vn.tpsc.it4u.security.CustomReporterDetails;
/**
 * ReporterService
 */
@Service
@ExtensionMethod({ StringUtils.class })
public class ReporterService {
    @Autowired
    private ReporterRepository reporterRepository;

    @Autowired
    private ModelMapper mapper;

    public boolean updateReporter(String userId,ReporterSummary updatingReporter, String bodyData) {
        JSONObject addData = new JSONObject(bodyData);
        Optional<Reporter> reporters = reporterRepository.findBySitename(userId);
        Reporter reporter = mapper.map(reporters.get(), Reporter.class);
        reporter.setSitename(updatingReporter.getSitename().isNullorEmpty() ? reporter.getSitename() : updatingReporter.getSitename());
        reporter.setWan1Ip(updatingReporter.getWan1Ip().isNullorEmpty() ? reporter.getWan1Ip() : updatingReporter.getWan1Ip());
        reporter.setWan1Provider(updatingReporter.getWan1Provider().isNullorEmpty() ? reporter.getWan1Provider() : updatingReporter.getWan1Provider());
        reporter.setWan1Status(updatingReporter.getWan1Status().isNullorEmpty() ? reporter.getWan1Status() : updatingReporter.getWan1Status());
        reporter.setWan1Uptime(updatingReporter.getWan1Uptime().isNullorEmpty() ? reporter.getWan1Uptime() : updatingReporter.getWan1Uptime());
        reporter.setWan2Ip(updatingReporter.getWan2Ip().isNullorEmpty() ? reporter.getWan2Ip() : updatingReporter.getWan2Ip());
        reporter.setWan2Provider(updatingReporter.getWan2Provider().isNullorEmpty() ? reporter.getWan2Provider() : updatingReporter.getWan2Provider());
        reporter.setWan2Status(updatingReporter.getWan2Status().isNullorEmpty() ? reporter.getWan2Status() : updatingReporter.getWan2Status());
        reporter.setWan2Uptime(updatingReporter.getWan2Uptime().isNullorEmpty() ? reporter.getWan2Uptime() : updatingReporter.getWan2Uptime());
        reporter.setWan3Ip(updatingReporter.getWan3Ip().isNullorEmpty() ? reporter.getWan3Ip() : updatingReporter.getWan3Ip());
        reporter.setWan3Provider(updatingReporter.getWan3Provider().isNullorEmpty() ? reporter.getWan3Provider() : updatingReporter.getWan3Provider());
        reporter.setWan3Status(updatingReporter.getWan3Status().isNullorEmpty() ? reporter.getWan3Status() : updatingReporter.getWan3Status());
        reporter.setWan3Uptime(updatingReporter.getWan3Uptime().isNullorEmpty() ? reporter.getWan3Uptime() : updatingReporter.getWan3Uptime());
        reporter.setWan4Ip(updatingReporter.getWan4Ip().isNullorEmpty() ? reporter.getWan4Ip() : updatingReporter.getWan4Ip());
        reporter.setWan4Provider(updatingReporter.getWan4Provider().isNullorEmpty() ? reporter.getWan4Provider() : updatingReporter.getWan4Provider());
        reporter.setWan4Status(updatingReporter.getWan4Status().isNullorEmpty() ? reporter.getWan4Status() : updatingReporter.getWan4Status());
        reporter.setWan4Uptime(updatingReporter.getWan4Uptime().isNullorEmpty() ? reporter.getWan4Uptime() : updatingReporter.getWan4Uptime());
        reporter.setUptimeLb(updatingReporter.getUptimeLb().isNullorEmpty() ? reporter.getUptimeLb() : updatingReporter.getUptimeLb());
        try {
            reporter.setAPConnected(addData.getString("APConnected").isNullorEmpty() ? reporter.getAPConnected() : addData.getString("APConnected"));
            reporter.setAPDisconnected(addData.getString("APDisconnected").isNullorEmpty() ? reporter.getAPDisconnected() : addData.getString("APDisconnected"));
            reporter.setUpload(addData.getString("upload").isNullorEmpty() ? reporter.getUpload() : addData.getString("upload"));
            reporter.setDownload(addData.getString("download").isNullorEmpty() ? reporter.getDownload() : addData.getString("download"));
        } catch (Exception e) {
            //TODO: handle exception
        }
        reporterRepository.save(reporter);
        return true;
    }

    public List<ReporterSummary> findByService(String service) {
        List<Reporter> reporters = reporterRepository.findByGroupClient(service);
        List<ReporterSummary> infos = reporters.stream().map(sitename -> new ReporterSummary(
            sitename.getId(),
                sitename.getSitename(),
                sitename.getWan1Ip(), 
                sitename.getWan1Provider(), 
                sitename.getWan1Status(),
                sitename.getWan1Uptime(),
                sitename.getWan2Ip(), 
                sitename.getWan2Provider(), 
                sitename.getWan2Status(),
                sitename.getWan2Uptime(),
                sitename.getWan3Ip(), 
                sitename.getWan3Provider(), 
                sitename.getWan3Status(),
                sitename.getWan3Uptime(),
                sitename.getWan4Ip(), 
                sitename.getWan4Provider(), 
                sitename.getWan4Status(),
                sitename.getWan4Uptime(),
                sitename.getUptimeLb(),
                sitename.getUpload(),
                sitename.getDownload(),
                sitename.getAPConnected(),
                sitename.getAPDisconnected(),
                sitename.getGroupClient()))
                .collect(Collectors.toList());
            return infos;
    }

    public List<ReporterSummary> findById(List<Long> sitenameId) {
        List<Reporter> reporters = reporterRepository.findByIdIn(sitenameId);
        List<ReporterSummary> sitenames = reporters.stream().map(sitename -> new ReporterSummary(
                sitename.getId(),
                sitename.getSitename(),
                sitename.getWan1Ip(), 
                sitename.getWan1Provider(), 
                sitename.getWan1Status(),
                sitename.getWan1Uptime(),
                sitename.getWan2Ip(), 
                sitename.getWan2Provider(), 
                sitename.getWan2Status(),
                sitename.getWan2Uptime(),
                sitename.getWan3Ip(), 
                sitename.getWan3Provider(), 
                sitename.getWan3Status(),
                sitename.getWan3Uptime(),
                sitename.getWan4Ip(), 
                sitename.getWan4Provider(), 
                sitename.getWan4Status(),
                sitename.getWan4Uptime(),
                sitename.getUptimeLb(),
                sitename.getUpload(),
                sitename.getDownload(),
                sitename.getAPConnected(),
                sitename.getAPDisconnected(),
                sitename.getGroupClient()
        )).collect(Collectors.toList());
        return sitenames;
    }

    public List<ReporterSummary> findAll() {
        List<Reporter> reporters = reporterRepository.findAll();
        
        List<ReporterSummary> listReporter = reporters.stream().map(reporter -> 
                new ReporterSummary(
                    reporter.getId(),
                    reporter.getSitename(),
                    reporter.getWan1Ip(), 
                    reporter.getWan1Provider(), 
                    reporter.getWan1Status(),
                    reporter.getWan1Uptime(),
                    reporter.getWan2Ip(), 
                    reporter.getWan2Provider(), 
                    reporter.getWan2Status(),
                    reporter.getWan2Uptime(),
                    reporter.getWan3Ip(), 
                    reporter.getWan3Provider(), 
                    reporter.getWan3Status(),
                    reporter.getWan3Uptime(),
                    reporter.getWan4Ip(), 
                    reporter.getWan4Provider(), 
                    reporter.getWan4Status(),
                    reporter.getWan4Uptime(),
                    reporter.getUptimeLb(),
                    reporter.getUpload(),
                    reporter.getDownload(),
                    reporter.getAPConnected(),
                    reporter.getAPDisconnected(),
                    reporter.getGroupClient()
                ))
            .collect(Collectors.toList());
        return listReporter;
    }

    public Boolean deleteReporter(Long sitenameId) {
        reporterRepository.deleteById(sitenameId);
        return true;
    }




}