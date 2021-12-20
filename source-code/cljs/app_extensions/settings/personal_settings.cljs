
(ns app-extensions.settings.personal-settings
    (:require [mid-fruits.css       :as css]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-user.api       :as user]
              [app-extensions.settings.cookie-settings :rename {view cookie-settings}]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:user-email-address       (r user/get-user-email-address       db)
   :user-first-name          (r user/get-user-first-name          db)
   :user-last-name           (r user/get-user-last-name           db)
   :user-name                (r user/get-user-name                db)
   :user-profile-picture-url (r user/get-user-profile-picture-url db)
   :user-phone-number        (r user/get-user-phone-number        db)})

(a/reg-sub ::get-body-props get-body-props)



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-profile-picture
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) body-id
  ; @param (map) body-props
  ;  {:user-profile-picture-url (string)}
  ;
  ; @return (hiccup)
  [_ {:keys [user-profile-picture-url]}]
 [elements/column ::user-profile-picture
                  {:content [:<> [:div.x-user-profile-picture {:style {:backgroundImage (css/url user-profile-picture-url)}}]
                                 [elements/button ::change-profile-picture-button
                                                  {:label     :change-profile-picture
                                                   :preset    :default-button
                                                   :font-size :xs}]]}])

(defn- user-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
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
  [_ _]
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
  [_ _]
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
  [_ _]
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
  [_ _]
  [:<> [elements/label ::user-pin-label
                       {:content   :pin
                        :color     :muted
                        :layout    :fit
                        :font-size :xs}]
       [elements/button ::user-pin-button
                        {:label  "••••"
                         :preset :default-button
                         :layout :fit}]])

(defn- personal-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:<> [elements/horizontal-separator {:size :s}]
       [user-profile-picture body-id body-props]
       [elements/horizontal-separator {:size :s}]
       [elements/horizontal-line      {:color :highlight}]
       [elements/horizontal-separator {:size :l}]
      [:div {:style {:width "100%"}}
       [user-name            body-id]
       [elements/horizontal-separator {:size :l}]
       [user-email-address   body-id]
       [elements/horizontal-separator {:size :l}]
       [user-phone-number    body-id]
       [elements/horizontal-separator {:size :l}]
       [user-password        body-id]
       [elements/horizontal-separator {:size :l}]
       [user-pin             body-id]
       [elements/horizontal-separator {:size :l}]
       [elements/button      ::delete-user-account-button
                             {:label :delete-user-account!
                              :preset :secondary-button}]
       [elements/button      ::clear-user-data-button
                             {:label :clear-user-data!
                              :preset :secondary-button}]]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber body-id
                         {:component  #'personal-settings
                          :subscriber [::get-body-props]}])
