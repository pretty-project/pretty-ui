
(ns pretty-website.follow-us-links.attributes
    (:require [fruits.href.api                      :as href]
              [pretty-build-kit.api                 :as pretty-build-kit]
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
  ; {:class (keyword or keywords in vector)}
  [_ links-props]
  (-> {:class :pw-follow-us-links--body}
      (pretty-build-kit/indent-attributes links-props)
      (pretty-build-kit/style-attributes  links-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn links-attributes
  ; @ignore
  ;
  ; @param (keyword) links-id
  ; @param (map) links-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ links-props]
  (-> {:class :pw-follow-us-links}
      (pretty-build-kit/class-attributes   links-props)
      (pretty-build-kit/outdent-attributes links-props)
      (pretty-build-kit/state-attributes   links-props)))
