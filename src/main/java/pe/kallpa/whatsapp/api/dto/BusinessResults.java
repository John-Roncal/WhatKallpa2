package pe.kallpa.whatsapp.api.dto;

import java.util.List;

public record BusinessResults(int cantidad, List<BusinessResult> negocios) {
}
