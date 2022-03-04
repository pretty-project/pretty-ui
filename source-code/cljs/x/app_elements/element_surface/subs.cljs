
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-surface.subs
    (:require [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (r element/get-element-prop db element-id :surface-visible?))

(defn surface-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (let [surface-visible? (r surface-visible? db element-id)]
       (not surface-visible?)))

(defn get-surface-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  ;  {:surface-visible? (boolean)}
  [db [_ element-id]]
  (if-let [surface-visible? (r surface-visible? db element-id)]
          {:surface-visible? surface-visible?}))
