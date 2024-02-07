
(ns pretty-website.follow-us-links.attributes
    (:require [fruits.href.api                      :as href]
              [pretty-attributes.api                :as pretty-attributes]
              [pretty-website.follow-us-links.utils :as follow-us-links.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn links-link-attributes
  ; @ignore
  ;
  ; @param (keyword) links-id
  ; @param (map) links-props
  ; @param (vector) link-props
  ; [(keyword) provider
  ;  (string) link]
  ;
  ; @return (map)
  ; {}
  [_ _ [provider link]]
  {:class  [:pw-follow-us-links--link :fab (follow-us-links.utils/provider->fa-icon-class provider)]
   :href   (href/https-uri link)
   :target "_blank"})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn links-body-attributes
  ; @ignore
  ;
  ; @param (keyword) links-id
  ; @param (map) links-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ links-props]
  (-> {:class :pw-follow-us-links--body}
      (pretty-attributes/indent-attributes links-props)
      (pretty-attributes/style-attributes  links-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn links-attributes
  ; @ignore
  ;
  ; @param (keyword) links-id
  ; @param (map) links-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ links-props]
  (-> {:class :pw-follow-us-links}
      (pretty-attributes/class-attributes  links-props)
      (pretty-attributes/outdent-attributes links-props)
      (pretty-attributes/state-attributes  links-props)
      (pretty-attributes/theme-attributes   links-props)))
