
(ns pretty-website.contacts.views
    (:require [fruits.hiccup.api                  :as hiccup]
              [fruits.random.api                  :as random]
              [metamorphic-content.api            :as metamorphic-content]
              [pretty-elements.engine.api                  :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-website.contacts.attributes :as contacts.attributes]
              [pretty-website.contacts.prototypes :as contacts.prototypes]
              [reagent.api                        :as reagent]))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- component-lifecycles
  ; @ignore
  ;
  ; @param (keyword) contacts-id
  ; @param (map) contacts-props
  [contacts-id contacts-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    contacts-id contacts-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount contacts-id contacts-props))
                       :reagent-render         (fn [_ contacts-props] [contacts contacts-id contacts-props])}))

(defn component
  ; @param (keyword)(opt) contacts-id
  ; @param (map) contacts-props
  ; {:contact-groups (maps in vector)(opt)
  ;   [{:addresses (strings in vector)(opt)
  ;     :email-addresses (strings in vector)(opt)
  ;     :info (metamorphic-content)(opt)
  ;     :label (metamorphic-content)(opt)
  ;     :phone-numbers (numbers or strings in vector)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;   ...
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [contacts {...}]
  ;
  ; @usage
  ; [contacts :my-contacts {...}]
  ([contacts-props]
   [component (random/generate-keyword) contacts-props])

  ([contacts-id contacts-props]
   ; @note (tutorials#parametering)
   (fn [_ contacts-props]
       (let [contacts-props (pretty-presets.engine/apply-preset           contacts-id contacts-props)
             contacts-props (contacts.prototypes/contacts-props-prototype contacts-id contacts-props)]
            [component-lifecycles contacts-id contacts-props]))))
