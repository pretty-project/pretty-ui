
(ns website.contacts.views
    (:require [elements.api :as elements]
              [href.api     :as href]
              [noop.api     :refer [return]]
              [random.api   :as random]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts-data-information
  ; @param (keyword) component-id
  ; @param (map) component-props
  [_ _]
  (if-let [contacts-data-information @(r/subscribe [:contents.handler/get-parsed-content [:website-contacts :handler/transfered-content :contacts-data-information]])]
          [:div {:id :mt-contacts--contacts-data-information} contacts-data-information]))

(defn- phone-number-link
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; @param (string) phone-number
  [_ _ phone-number]
  [:a {:class :mt-contacts--phone-number
       :href  (href/phone-number phone-number)}
      (str phone-number)])

(defn- email-address-link
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; @param (string) email-address
  [_ _ email-address]
  [:a {:class :mt-contacts--email-address
       :href  (href/email-address email-address)}
      (str email-address)])

(defn- contact-group
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; @param (map) group-props
  ; {:email-addresses (strings in vector)(opt)
  ;  :label (string)(opt)
  ;  :phone-numbers (strings in vector)(opt)}
  [component-id component-props {:keys [email-addresses label phone-numbers]}]
  [:div {:class :mt-contacts--contact-group}
        (if label [:div {:class :mt-contacts--contact-group-label} label])
        (letfn [(f [phone-numbers phone-number]
                   (if phone-number (conj   phone-numbers [phone-number-link component-id component-props phone-number])
                                    (return phone-numbers)))]
               (reduce f [:<>] phone-numbers))
        (letfn [(f [email-addresses email-address]
                   (if email-address (conj   email-addresses [email-address-link component-id component-props email-address])
                                     (return email-addresses)))]
               (reduce f [:<>] email-addresses))])

(defn- contact-groups
  ; @param (keyword) component-id
  ; @param (map) component-props
  [component-id component-props]
  (let [contact-groups @(r/subscribe [:x.db/get-item [:website-contacts :handler/transfered-content :contact-groups]])]
       [:div {:id :mt-contacts--contact-groups}
             (letfn [(f [groups group-props]
                        (conj groups [contact-group component-id component-props group-props]))]
                    (reduce f [:<>] contact-groups))]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- address-data-information
  ; @param (keyword) component-id
  ; @param (map) component-props
  [_ _]
  (if-let [address-data-information @(r/subscribe [:contents.handler/get-parsed-content [:website-contacts :handler/transfered-content :address-data-information]])]
          [:div {:id :mt-contacts--address-data-information} address-data-information]))

(defn- company-address-link
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; @param (string) company-address
  [_ _ company-address]
  [:a {:class :mt-contacts--company-address
       :href  (href/address company-address)}
      (str company-address)])

(defn- address-group
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; @param (map) group-props
  ; {:company-address (string)(opt)
  ;  :label (string)(opt)}
  [component-id component-props {:keys [company-address label]}]
  [:div {:class :mt-contacts--address-group}
        (if label           [:div {:class :mt-contacts--address-group-label} label])
        (if company-address [company-address-link component-id component-props company-address])])

(defn- address-groups
  ; @param (keyword) component-id
  ; @param (map) component-props
  [component-id component-props]
  (let [address-groups @(r/subscribe [:x.db/get-item [:website-contacts :handler/transfered-content :address-groups]])]
       [:div {:id :mt-contacts--address-groups}
             (letfn [(f [groups group-props]
                        (conj groups [address-group component-id component-props group-props]))]
                    (reduce f [:<>] address-groups))]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts
  ; @param (keyword) component-id
  ; @param (map) component-props
  [component-id component-props]
  [:div {:id :mt-contacts}
        [contact-groups            component-id component-props]
        [contacts-data-information component-id component-props]
        [address-groups            component-id component-props]
        [address-data-information  component-id component-props]])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ; {}
  ;
  ; @usage
  ; [contacts {...}]
  ;
  ; @usage
  ; [contacts :my-contacts {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [contacts component-id component-props]))
