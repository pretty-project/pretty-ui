
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-prohibitor.events
    (:require [mid-fruits.map :refer [dissoc-in]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-scroll-prohibitions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:environment :scroll-prohibitor/data-items]))
