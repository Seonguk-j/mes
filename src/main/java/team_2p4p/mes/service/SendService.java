package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import team_2p4p.mes.repository.SendRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class SendService {

    private final SendRepository sendRepository;

}
