
; WARNING! THIS IS AN OUTDATED VERSION OF A MONO-TEMPLATE FILE!



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-db.api
    (:require [project-emulator.server-db.query-handler :as query-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def process! query-handler/process!)
