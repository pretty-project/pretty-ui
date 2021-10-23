
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.6.2
; Compatibility: x4.3.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.clickable
    (:require [mid-fruits.candy                 :refer [param]]
              [mid-fruits.map                   :as map]
              [x.app-components.api             :as components]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.focusable  :as focusable]
              [x.app-elements.engine.targetable :as targetable]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-click-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (function)
  [element-id]
  #(a/dispatch [:x.app-elements/->element-clicked element-id]))

(defn clickable-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)
  ;   :href (string)(opt)
  ;   :targetable? (boolean)(opt)
  ;   :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :href (string)
  ;   :id (string)
  ;   :on-click (function)
  ;   :on-mouse-up (function)
  ;   :title (string)}
  [element-id {:keys [disabled? href targetable? tooltip]}]
  (cond-> (param {})
          (boolean disabled?)   (merge {:disabled true})
          (not     disabled?)   (merge {:href        (param href)
                                        :on-click    (on-click-function               element-id)
                                        :on-mouse-up (focusable/blur-element-function element-id)
                                        :title       (components/content              {:content tooltip})})
          (boolean targetable?) (merge {:id (targetable/element-id->target-id element-id)})))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-keypress-handled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (map/nonempty? (r element/get-element-prop db element-id :keypress)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/init-clickable!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ element-id]]
      (if-let [keypress (r element/get-element-prop db element-id :keypress)]
              [:x.app-environment.keypress-handler/reg-keypress-event! element-id
               {:key-code   (:key-code keypress)
                :on-keydown [:x.app-elements/->key-pressed  element-id]
                :on-keyup   [:x.app-elements/->key-released element-id]
                :required?  (:required? keypress)}])))

(a/reg-event-fx
  :x.app-elements/destruct-clickable!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ element-id {:keys [keypress]}]]
      (if (some? keypress)
          [:x.app-environment.keypress-handler/remove-keypress-event! element-id])))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/->element-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ element-id]]
      (if-let [on-click-event (r element/get-element-prop db element-id :on-click)]
              {:dispatch-n [on-click-event]})))

(a/reg-event-fx
  :x.app-elements/->key-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [_ [_ element-id]]
      {:dispatch-if [(targetable/element-id->target-enabled? element-id)
                     [:x.app-elements/focus-element! element-id]]}))

(a/reg-event-fx
  :x.app-elements/->key-released
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [_ [_ element-id]]
      {:dispatch    [:x.app-elements/blur-element! element-id]
       :dispatch-if [(targetable/element-id->target-enabled? element-id)
                     [:x.app-elements/->element-clicked      element-id]]}))
