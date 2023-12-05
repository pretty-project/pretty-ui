
(ns pretty-website.contacts.views
    (:require [hiccup.api                         :as hiccup]
              [metamorphic-content.api            :as metamorphic-content]
              [pretty-website.contacts.attributes :as contacts.attributes]
              [pretty-website.contacts.prototypes :as contacts.prototypes]
              [pretty-presets.api                  :as pretty-presets]
              [random.api                         :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contact-group
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ; @param (map) group-props
  ; {:addresses (strings in vector)(opt)
  ;  :email-addresses (strings in vector)(opt)
  ;  :info (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :phone-numbers (numbers or strings in vector)(opt)}
  [contacts-id contacts-props {:keys [addresses email-addresses info label phone-numbers] :as group-props}]
  [:div {:class :pw-contacts--contact-group}
        ; Contact group label
        (if label [:div (contacts.attributes/contact-group-label-attributes contacts-id contacts-props group-props)
                        (metamorphic-content/compose label)])
        ; Contact group phone numbers
        (letfn [(f0 [%] [:a (contacts.attributes/contact-group-phone-number-attributes contacts-id contacts-props group-props %)
                            (str %)])]
               (hiccup/put-with [:<>] phone-numbers f0))
        ; Contact group email addresses
        (letfn [(f0 [%] [:a (contacts.attributes/contact-group-email-address-attributes contacts-id contacts-props group-props %)
                            (str %)])]
               (hiccup/put-with [:<>] email-addresses f0))
        ; Contact group addresses
        (letfn [(f0 [%] [:a (contacts.attributes/contact-group-address-attributes contacts-id contacts-props group-props %)
                            (str %)])]
               (hiccup/put-with [:<>] addresses f0))
        ; Contact group info
        (if info [:div (contacts.attributes/contact-group-info-attributes contacts-id contacts-props group-props)
                       (hiccup/parse-newlines [:<> (metamorphic-content/compose info)])])])

(defn- contacts
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  ; {:contact-groups (maps in vector)(opt)}
  [contacts-id {:keys [contact-groups] :as contacts-props}]
  [:div (contacts.attributes/contacts-attributes contacts-id contacts-props)
        [:div (contacts.attributes/contacts-body-attributes contacts-id contacts-props)
              (if-not (empty? contact-groups)
                      (letfn [(f0 [group-props] [contact-group contacts-id contacts-props group-props])]
                             (hiccup/put-with [:<>] contact-groups f0)))]])

(defn component
  ; @param (keyword)(opt) contacts-id
  ; @param (map) contacts-props
  ; {:contact-groups (maps in vector)(opt)
  ;   [{:addresses (strings in vector)(opt)
  ;     :email-addresses (strings in vector)(opt)
  ;     :info (metamorphic-content)(opt)
  ;     :label (metamorphic-content)(opt)
  ;     :phone-numbers (numbers or strings in vector)(opt)}]
  ;  :class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [contacts {...}]
  ;
  ; @usage
  ; [contacts :my-contacts {...}]
  ([contacts-props]
   [component (random/generate-keyword) contacts-props])

  ([contacts-id contacts-props]
   (fn [_ contacts-props] ; XXX#0106 (README.md#parametering)
       (let [contacts-props (pretty-presets/apply-preset contacts-props)]
            ; contacts-props (contacts.prototypes/contacts-props-prototype contacts-props)
            [contacts contacts-id contacts-props]))))
