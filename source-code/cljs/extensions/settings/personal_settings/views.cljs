
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.personal-settings.views
    (:require [mid-fruits.css     :as css]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- change-profile-picture-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::change-profile-picture-button
                   {:label     :change-profile-picture!
                    :preset    :default-button
                    :font-size :xs}])

(defn- user-profile-picture
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [user-profile-picture @(a/subscribe [:user/get-user-profile-picture])]
       [:div.x-user-profile-picture {:style {:backgroundImage (css/url user-profile-picture)}}]))

(defn- user-profile-picture-block
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/column ::user-profile-picture-block
                   {:content [:<> [user-profile-picture]
                                  [change-profile-picture-button]]}])

(defn- user-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-name-label
                       {:content   :name
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-name-button
                        {:label  "Tech Mono"
                         :preset :default-button
                         :layout :fit}]])

(defn- user-email-address
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-email-address-label
                       {:content   :email-address
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-email-address-button
                        {:label  "demo@monotech.hu"
                         :preset :default-button
                         :layout :fit}]])

(defn- user-phone-number
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-phone-number-label
                       {:content   :phone-number
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-phone-number-button
                        {:label  "+36301234567"
                         :preset :default-button
                         :layout :fit}]])

(defn- user-password
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-password-label
                       {:content   :password
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-password-button
                        {:label  "••••••••"
                         :preset :default-button
                         :layout :fit}]])

(defn- user-pin
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label ::user-pin-label
                       {:content   :pin
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-pin-button
                        {:label  "••••"
                         :preset :default-button
                         :layout :fit}]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [:<> [elements/horizontal-separator {:size :s}]
       [user-profile-picture-block]
       [elements/horizontal-separator {:size :s}]
       [elements/horizontal-line      {:color :highlight}]
       [elements/horizontal-separator {:size :l}]
      [:div {:style {:width "100%"}}
       [user-name]
       [elements/horizontal-separator {:size :l}]
       [user-email-address]
       [elements/horizontal-separator {:size :l}]
       [user-phone-number]
       [elements/horizontal-separator {:size :l}]
       [user-password]
       [elements/horizontal-separator {:size :l}]
       [user-pin]
       [elements/horizontal-separator {:size :l}]
       [elements/button ::delete-user-account-button
                        {:label :delete-user-account!
                         :preset :secondary-button}]
       [elements/button ::clear-user-data-button
                        {:label :clear-user-data!
                         :preset :secondary-button}]]])
