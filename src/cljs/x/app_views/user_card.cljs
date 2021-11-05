
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.2.2
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.user-card
    (:require [mid-fruits.css     :as css]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) card-props
  ;  {}
  ;
  ; @return (hiccup)
  [_ {:keys [user-email-address user-name user-profile-picture-url]}]
  [:div.x-user-card
    [:div.x-user-profile-picture {:style {:backgroundImage (css/url user-profile-picture-url)}}]
    [elements/separator {:size :s :orientation :horizontal}]
    [elements/label     {:content user-name :layout :fit :size :xl :font-weight :extra-bold}]
    [elements/label     {:content user-email-address :color :highlight :layout :fit :font-size :xs}]])
