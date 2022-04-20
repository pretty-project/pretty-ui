
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.error-page.views
    (:require [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:title (metamorphic-content)}
  [_ {:keys [title]}]
  [elements/text ::error-title
                 {:content          title
                  :font-size        :xxl
                  :font-weight      :bold
                  :horizontal-align :center
                  :indent           {:top :xxl :vertical :xs}
                  :selectable?      false}])

(defn- error-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:helper (metamorphic-content)}
  [_ {:keys [helper]}]
  [elements/text ::error-helper
                 {:content          helper
                  :font-size        :s
                  :horizontal-align :center
                  :indent           {:vertical :xs}
                  :selectable?      false}])

(defn- error-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:icon (keyword)(opt)}
  [_ {:keys [icon]}]
  (if icon [elements/icon ::error-icon
                          {:icon icon
                           :size :xxl}]))

(defn- go-back-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  [_ _]
  [elements/button ::go-back-button
                   {:indent   {:top :m}
                    :label    :back!
                    :on-click [:router/go-back!]}])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  [surface-id content-props]
  [:<> [elements/horizontal-separator {:size :xxl}]
       [error-icon     surface-id content-props]
       [error-title    surface-id content-props]
       [error-helper   surface-id content-props]
       [go-back-button surface-id content-props]])
