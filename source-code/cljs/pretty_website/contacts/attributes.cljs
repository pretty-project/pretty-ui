
(ns pretty-website.contacts.attributes
    (:require [fruits.href.api      :as href]
              [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn contact-group-label-attributes
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ _ _]
  {:class               :pw-contacts--contact-group--label
   :data-font-size      :m
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block})

(defn contact-group-info-attributes
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ _ _]
  {:class               :pw-contacts--contact-group--info
   :data-font-size      :s
   :data-letter-spacing :auto
   :data-line-height    :auto})

(defn contact-group-link-attributes
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ _ _]
  {:class               :pw-contacts--contact-group--link
   :data-click-effect   :opacity
   :data-font-size      :l
   :data-font-weight    :medium
   :data-hover-effect   :opacity
   :data-letter-spacing :auto
   :data-line-height    :text-block})

(defn contact-group-email-address-attributes
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ; @param (map) group-props
  ; @param (string) email-address
  ;
  ; @return (map)
  ; {:href (string)}
  [contacts-id contacts-props group-props email-address]
  (merge (contact-group-link-attributes contacts-id contacts-props group-props)
         {:href (href/email-address email-address)}))

(defn contact-group-address-attributes
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ; @param (map) group-props
  ; @param (string) address
  ;
  ; @return (map)
  ; {:href (string)
  ;  :target (string)}
  [contacts-id contacts-props group-props address]
  (merge (contact-group-link-attributes contacts-id contacts-props group-props)
         {:href   (href/google-maps-address address)
          :target "_blank"}))

(defn contact-group-phone-number-attributes
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ; @param (map) group-props
  ; @param (number or string) phone-number
  ;
  ; @return (map)
  ; {:href (string)}
  [contacts-id contacts-props group-props phone-number]
  (merge (contact-group-link-attributes contacts-id contacts-props group-props)
         {:href (href/phone-number phone-number)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn contacts-body-attributes
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ contacts-props]
  (-> {:class :pw-contacts--body}
      (pretty-css/indent-attributes contacts-props)
      (pretty-css/style-attributes  contacts-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn contacts-attributes
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ contacts-props]
  (-> {:class :pw-contacts}
      (pretty-css/class-attributes   contacts-props)
      (pretty-css/outdent-attributes contacts-props)
      (pretty-css/state-attributes   contacts-props)))
