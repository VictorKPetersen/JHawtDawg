#set page(
  paper: "a4",
  footer: context {
    let page_number = counter(page).at(here()).first()

    if page_number > 1 {
      align(center, counter(page).display())
    }
  }
)
#set heading(numbering: "1.")

#line(start: (0%, 5%), end: (8.5in, 5%), stroke: (thickness: 2pt))

#align(horizon + left)[
  #text(size: 24pt, [Software Maintenance \ Note Report])

  Software Maintenance Report

  vipet23\@student.sdu.dk
]

#align(bottom + left)[#datetime.today().display()]

#import "@preview/codly:1.3.0": *
#import "@preview/codly-languages:0.1.1": *
#show: codly-init.with()
#codly(languages: codly-languages, zebra-fill: none, stroke: 2pt + gray)

#pagebreak()

#include "sections/ChangeRequest.typ"
#include "sections/ConceptLocation.typ"
#include "sections/ImpactAnalysis.typ"

#pagebreak()

#include "sections/Refactoring.typ"
#include "sections/Vertification.typ"
#include "sections/CI.typ"
#include "sections/Conclusion.typ"

#pagebreak()
#bibliography("bibliography.bib", style: "ieee")
