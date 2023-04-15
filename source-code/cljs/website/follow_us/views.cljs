
(ns website.follow-us.views
    (:require [hiccup.api                   :as hiccup]
              [random.api                   :as random]
              [website.follow-us.attributes :as follow-us.attributes]
              [website.follow-us.prototypes :as follow-us.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- follow-us
  ; @ignore
  ;
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:links (vectors in vector)}
  [component-id {:keys [links] :as component-props}]
  [:div (follow-us.attributes/component-attributes component-id component-props)
        [:div (follow-us.attributes/component-body-attributes component-id component-props)
              (letfn [(f [%] [:a (follow-us.attributes/component-link-attributes component-id component-props %)])]
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
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
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
  ;   Same as the :indent property
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [follow-us {...}]
  ;
  ; @usage
  ; [follow-us :my-follow-us {...}]
  ;
  ; @usage
  ; [follow-us {:links [[:facebook "facebook.com/my-profile"]
  ;                     [:instagram "instagram.com/my-profile"]]}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   (let [] ; component-props (follow-us.prototypes/component-props-prototype component-props)
        [follow-us component-id component-props])))
