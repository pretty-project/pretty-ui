
(ns pretty-website.follow-us-links.views
    (:require [fruits.hiccup.api                         :as hiccup]
              [fruits.random.api                         :as random]
              [pretty-elements.engine.api                         :as pretty-elements.engine]
              [pretty-presets.api                        :as pretty-presets]
              [pretty-website.follow-us-links.attributes :as follow-us-links.attributes]
              [pretty-website.follow-us-links.prototypes :as follow-us-links.prototypes]
              [reagent.api                               :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- follow-us-links
  ; @ignore
  ;
  ; @param (keyword) links-id
  ; @param (map) links-props
  ; {:links (vectors in vector)}
  [links-id {:keys [links] :as links-props}]
  [:div (follow-us-links.attributes/links-attributes links-id links-props)
        [:div (follow-us-links.attributes/links-body-attributes links-id links-props)
              (letfn [(f0 [%] [:a (follow-us-links.attributes/links-link-attributes links-id links-props %)])]
                     (hiccup/put-with [:<>] links f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- component-lifecycles
  ; @ignore
  ;
  ; @param (keyword) links-id
  ; @param (map) links-props
  [links-id links-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    links-id links-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount links-id links-props))
                       :reagent-render         (fn [_ links-props] [follow-us-links links-id links-props])}))

(defn component
  ; @important
  ; To use this component you must add the Font Awesome icon set to your project!
  ;
  ; @description
  ; This component uses Font Awesome brand icons for social media provider links.
  ; It converts the given provider name into Font Awesome icon class:
  ; :instagram => .fab.fa-instagram
  ;
  ; @param (keyword)(opt) links-id
  ; @param (map) links-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :links (vectors in vector)
  ;   [[(keyword) provider
  ;     (string) link]
  ;    [...]]
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [follow-us-links {...}]
  ;
  ; @usage
  ; [follow-us-links :my-follow-us-links {...}]
  ;
  ; @usage
  ; [follow-us-links {:links [[:facebook "facebook.com/my-profile"]
  ;                           [:instagram "instagram.com/my-profile"]]}]
  ([links-props]
   [component (random/generate-keyword) links-props])

  ([links-id links-props]
   ; @note (tutorials#parametering)
   (fn [_ links-props]
       (let [links-props (pretty-presets/apply-preset links-props)]
             ; links-props (follow-us-links.prototypes/links-props-prototype links-props)
            [component-lifecycles links-id links-props]))))
