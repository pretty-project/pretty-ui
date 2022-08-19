

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.surface-handler.events
    (:require [mid-fruits.candy                    :refer [param return]]
              [x.app-core.api                      :as a :refer [r]]
              [x.app-elements.surface-handler.subs :as surface-handler.subs]
              [x.app-elements.engine.element       :as element]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (if (r surface-handler.subs/surface-visible? db element-id)
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
