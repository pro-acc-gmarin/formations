--
-- Structure de la table `board`
--
CREATE TABLE IF NOT EXISTS `board` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_owner` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `linked_tasks` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;