
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.error-page.views
    (:require [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:title (metamorphic-content)}
  [_ {:keys [title]}]
  [elements/text ::error-title
                 {:content title :font-size :xxl :horizontal-align :center :layout :fit :selectable? false
                  :font-weight :bold :indent :both}])

(defn- error-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:helper (metamorphic-content)}
  [_ {:keys [helper]}]
  [elements/text ::error-helper
                 {:content helper :font-size :s :horizontal-align :center :layout :fit :selectable? false
                  :indent :both}])

(defn- error-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:icon (keyword)(opt)}
  [_ {:keys [icon]}]
  (if icon [elements/icon ::error-icon
                          {:icon icon :size :xxl}]))

(defn- go-back-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  [_ _]
  [elements/button ::go-back-button
                   {:label    :back!
                    :variant  :transparent
                    :on-click [:router/go-back!]}])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  [surface-id content-props]
  [:<> [elements/horizontal-separator {:size :xl}]
       [error-icon     surface-id content-props]
       [elements/horizontal-separator {:size :m}]
       [error-title    surface-id content-props]
       [error-helper   surface-id content-props]
       [elements/horizontal-separator {:size :m}]
       [go-back-button surface-id content-props]])
