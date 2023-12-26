
(ns pretty-website.follow-us-links.views
    (:require [fruits.hiccup.api                         :as hiccup]
              [fruits.random.api                         :as random]
              [pretty-presets.api                        :as pretty-presets]
              [pretty-website.follow-us-links.attributes :as follow-us-links.attributes]
              [pretty-website.follow-us-links.prototypes :as follow-us-links.prototypes]))

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

(defn component
  ; @important
  ; To use this component you must add the Font Awesome icon set to your project!
  ;
  ; @description
  ; This component uses Font Awesome brand icons for social media provider links.
  ; It converts the given provider name into an icon class:
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
   ; @note (tutorials#parametering)
   (fn [_ links-props]
       (let [links-props (pretty-presets/apply-preset links-props)]
             ; links-props (follow-us-links.prototypes/links-props-prototype links-props)
            [follow-us-links links-id links-props]))))
