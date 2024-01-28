
(ns pretty-website.follow-us-links.attributes
    (:require [fruits.href.api                      :as href]
              
              [pretty-website.follow-us-links.utils :as follow-us-links.utils]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.layout/indent-attributes links-props)
      (pretty-css.basic/style-attributes  links-props)))

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
      (pretty-css.basic/class-attributes   links-props)
      (pretty-css.layout/outdent-attributes links-props)
      (pretty-css.basic/state-attributes   links-props)
      (pretty-css.appearance/theme-attributes   links-props)))
