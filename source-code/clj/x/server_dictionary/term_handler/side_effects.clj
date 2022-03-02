
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.engine
    (:require [server-fruits.io  :as io]
              [x.server-core.api :as a]
              [x.server-dictionary.term-handler.engine :as term-handler.engine]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- import-project-dictionary!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (a/dispatch [:dictionary/add-terms! (io/read-edn-file term-handler.engine/PROJECT-DICTIONARY-FILEPATH)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :dictionary/import-project-dictionary! import-project-dictionary!)
