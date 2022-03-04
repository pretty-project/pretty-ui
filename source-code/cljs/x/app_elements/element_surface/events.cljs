
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-surface.events
    (:require [mid-fruits.candy                    :refer [param return]]
              [x.app-core.api                      :as a :refer [r]]
              [x.app-elements.element-surface.subs :as element-surface.subs]
              [x.app-elements.engine.element       :as element]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (if (r element-surface.subs/surface-visible? db element-id)
      (as-> db % (r element/set-element-prop!    % element-id :surface-visible? false)
                 (r element/remove-element-prop! % element-id :surface-props))
      (return db)))

(defn show-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (r element/set-element-prop! db element-id :surface-visible? true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/hide-surface! hide-surface!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/show-surface! show-surface!)
