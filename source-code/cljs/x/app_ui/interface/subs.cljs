
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.interface.subs
    (:require [x.app-core.api :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-interface
  ; @usage
  ;  (r ui/get-interface db)
  ;
  ; @return (keyword)
  ;  :application-ui, :website-ui
  [db _]
  (get-in db [:ui :interface/meta-items :interface]))

(defn application-interface?
  ; @usage
  ;  (r ui/application-interface? db)
  ;
  ; @return (boolean)
  [db _]
  (= :application-ui (r get-interface db)))

(defn website-interface?
  ; @usage
  ;  (r ui/website-interface? db)
  ;
  ; @return (boolean)
  [db _]
  (= :website-ui (r get-interface db)))
