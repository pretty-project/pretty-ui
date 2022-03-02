
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.term-handler.effects
    (:require [x.server-core.api :as a]
              [x.mid-dictionary.books :refer [BOOKS]]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :dictionary/init-dictionary!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:fx       [:dictionary/import-project-dictionary!]
       :dispatch [:dictionary/add-terms! BOOKS]}))
