
(ns pretty-website.follow-us-links.views
    (:require [hiccup.api                          :as hiccup]
              [pretty-website.follow-us-links.attributes :as follow-us-links.attributes]
              [pretty-website.follow-us-links.prototypes :as follow-us-links.prototypes]
              [pretty-presets.api                  :as pretty-presets]
              [random.api                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- follow-us-links
  ; @ignore
  ;
  ; @param (keyword) links-id
  ; @param (map) links-props
  ; {:links (vectors in vector)}
  [links-id {:keys [links] :as links-props}]
  [:div (follow-us-links-links.attributes/links-attributes links-id links-props)
        [:div (follow-us-links-links.attributes/links-body-attributes links-id links-props)
              (letfn [(f [%] [:a (follow-us-links-links.attributes/links-link-attributes links-id links-props %)])]
                     (hiccup/put-with [:<>] links f))]])

(defn component
  ; @warning
  ; To use this component you have to add the Font Awesome icon set to your project!
  ;
  ; @description
  ; This component uses Font Awesome brand icons for social media provider links.
  ; It converts the given provider name to an icon class:
  ; :instagram => .fab.fa-instagram
  ;
  ; @param (keyword)(opt) links-id
  ; @param (map) links-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :links (vectors in vector)
  ;   [[(keyword) provider
  ;     (string) link]
  ;    [...]]
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
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
   (fn [_ links-props] ; XXX#0106 (README.md#parametering)
       (let [links-props (pretty-presets/apply-preset links-props)]
            ; links-props (follow-us-links.prototypes/links-props-prototype links-props)
            [follow-us-links links-id links-props]))))
