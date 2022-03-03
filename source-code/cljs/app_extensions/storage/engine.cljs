
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.engine
    (:require [mid-extensions.storage.engine :as engine]
              [x.app-core.api                :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.storage.engine
(def ROOT-DIRECTORY-ID engine/ROOT-DIRECTORY-ID)
