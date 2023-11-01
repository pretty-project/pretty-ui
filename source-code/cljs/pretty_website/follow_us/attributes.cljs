
(ns pretty-website.follow-us.attributes
    (:require [href.api                       :as href]
              [pretty-css.api                 :as pretty-css]
              [pretty-website.follow-us.utils :as follow-us.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-link-attributes
  ; @ignore
  ;
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:style (map)(opt)}
  ; @param (vector) link-props
  ; [(keyword) provider
  ;  (string) link]
  ;
  ; @return (map)
  ; {}
  [_ _ [provider link]]
  {:class  [:pw-follow-us--link :fab (follow-us.utils/provider->fa-icon-class provider)]
   :href   (href/https-address link)
   :target "_blank"})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-body-attributes
  ; @ignore
  ;
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as component-props}]
  (-> {:class :pw-follow-us--body
       :style style}
      (pretty-css/indent-attributes component-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-attributes
  ; @ignore
  ;
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ component-props]
  (-> {:class :pw-follow-us}
      (pretty-css/default-attributes component-props)
      (pretty-css/outdent-attributes component-props)))
