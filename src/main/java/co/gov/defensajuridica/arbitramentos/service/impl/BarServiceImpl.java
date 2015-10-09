package co.gov.defensajuridica.arbitramentos.service.impl;

import co.gov.defensajuridica.arbitramentos.service.BarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BarServiceImpl implements BarService {

    private final Logger log = LoggerFactory.getLogger(BarServiceImpl.class);

}
