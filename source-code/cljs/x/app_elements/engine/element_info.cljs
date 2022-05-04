
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.27
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-info
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn info-text-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (get-in db [:elements :element-handler/meta-items element-id :info-text-visible?]))

(a/reg-sub :elements/info-text-visible? info-text-visible?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-info-text!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (update-in db [:elements :element-handler/meta-items element-id :info-text-visible?] not))

(a/reg-event-db :elements/toggle-info-text! toggle-info-text!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:helper (metamorphic-content)}
  [_ {:keys [helper]}]
  (if helper [:div.x-element--helper (components/content helper)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn info-text-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:info-text (metamorphic-content)(opt)}
  [element-id {:keys [info-text]}]
  (if-let [info-text-visible? @(a/subscribe [:elements/info-text-visible? element-id])]
          [:div.x-element--info-text--content (components/content info-text)]))

(defn info-text-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  [element-id _]
  [:button.x-element--info-text--button {:on-click    #(a/dispatch [:elements/toggle-info-text! element-id])
                                         :on-mouse-up #(environment/blur-element!)}
                                        (param :info_outline)])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {}
  [element-id {:keys [info-text label required?] :as element-props}]
  [:div.x-element--header (if label [:div.x-element--label (components/content label)
                                                           (if required? [:span.x-element--label-asterisk "*"])])
                          (if info-text [info-text-button  element-id element-props])
                          (if info-text [info-text-content element-id element-props])])
