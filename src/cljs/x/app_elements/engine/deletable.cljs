
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.5.0
; Compatibility: x4.3.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.deletable
    (:require [mid-fruits.candy                 :refer [param]]
              [mid-fruits.map                   :as map]
              [x.app-components.api             :as components]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.focusable  :as focusable]
              [x.app-elements.engine.targetable :as targetable]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! DEPRECATED! DO NOT USE!
; XXX#7601
(defn on-delete-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (function)
  [element-id]
  #(a/dispatch [:x.app-elements/->element-deleted element-id]))
; WARNING! DEPRECATED! DO NOT USE!

(defn deletable-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :on-click (function)}
  [element-id {:keys [disabled? on-delete targetable? tooltip]}]
  (cond-> (param {})
          (boolean disabled?) (merge {:disabled true})
          (not     disabled?) (merge {:on-click   #(a/dispatch on-delete)
                                      :on-mouse-up (focusable/blur-element-function element-id)
                                      :title       (components/content {:content :remove!})})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! DEPRECATED! DO NOT USE!
; XXX#7601
;
; Minél kevesebb element használjon feliratkozást! Ahol nem feltétlenül szükséges
; ott ne Re-Frame esemény kezelje az interakciókat!
(a/reg-event-fx
  :x.app-elements/->element-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ element-id]]
    (if-let [on-delete-event (r element/get-element-prop db element-id :on-delete)]
            {:dispatch on-delete-event})))
; WARNING! DEPRECATED! DO NOT USE!
