# Rangement_de_motifs_par_optimisation_multicritère
## Une methode d'apprentissage basée sur l'optimisation multicritère pour le rangement des motifs en fouille de données (EGC 2022).
### Résumé 

La découverte de motifs pertinents est une tâche difficile en fouille de données. 
D’une part, des approches ont été proposées pour apprendre automa tiquement des fonctions de rangement de motifs spécifiques à l’utilisateur.
Ces approches sont souvent efficaces en qualité, mais très coûteuses en temps d’exécution. D’autre part, de nombreuses mesures d’intérêt sont utilisées
pour évaluer l’intérêt des motifs dans le but de se rapprocher le plus possible du rangement de l’utilisateur. Dans cet article, nous formulons le problème 
d’apprentissage des fonctions de rangement des motifs comme un problème d’optimisation multicritère. L’approche proposée permet d’agréger des mesures d’intérêt 
en une fonction linéaire pondérée dont les poids sont calculés via la méthode AHP (Analytic Hierarchy Process). Des expérimentations menées sur de nombreux jeux d
e données montrent que notre approche réduit drastiquement le temps d’exécution, tout en assurant un rangement comparable à celui des approches existantes.

## A learning method based on multi-criteria optimization for pattern ordering in data mining (EGC 2022).


### Abstract

The discovery of interesting patterns is a challenging task in data mining. On one hand,
approaches have been proposed to automatically learn user-specific ranking functions over
patterns. These approaches are often efficient in accuracy, but very expensive in execution time.
On the other hand, many interest measures are used to evaluate the interest of patterns with the
goal of coming as close as possible to a user-specific ranking. In this paper, we express the
learning of a pattern ranking function as a multicriteria optimization problem. The proposed
approach aggregates all measures into a single weighted linear function which coefficients are
computed via the Analytic Hierarchy Process (AHP). Experiments conducted on many datasets
show that our approach drastically reduces the execution time, while ensuring a high quality
ranking compared to existing approaches.

## Experimentation / Experiments
### Datasets
Zoo|Vote|Anneal|Chess|Mushroom|T10I10D100K|T40I10D100K|Pumsb|Retail|
### Passive Learning 
java -jar AHPRank_Run_Passive.jar <dataset> <nb runs> <timeout (seconds)>
### Active Learning
java -jar AHPRank_Run_Active.jar <dataset> <nb iterations> <timeout (seconds)>

### Results
Results are found in the folder "Results" each column represents the following :
ρ RankingSVM,ρ AHPRank,Sigma SVM,Sigma AHP,R@10% RankingSVM,R@1% RankingSVM,R@10% AHPRank,R@1% AHPRank,Learning Time RankingSVM,Learning Time AHPRank,Heuristic Selection Time RankingSVM,Heuristic Selection Time AHPRANK

