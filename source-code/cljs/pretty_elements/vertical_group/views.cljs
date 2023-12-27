
(ns pretty-elements.vertical-group.views
    (:require [fruits.hiccup.api                         :as hiccup]
              [fruits.random.api                         :as random]
              [pretty-elements.vertical-group.attributes :as vertical-group.attributes]
              [pretty-elements.vertical-group.prototypes :as vertical-group.prototypes]
              [pretty-presets.api                        :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id {:keys [default-props element group-items] :as group-props}]
  [:div (vertical-group.attributes/group-attributes group-id group-props)
        [:div (vertical-group.attributes/group-body-attributes group-id group-props)
              (letfn [(f0 [group-item] [element (merge default-props group-item)])]
                     (hiccup/put-with [:<>] group-items f0))]])

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :default-props (map)(opt)
  ;  :element (Reagent component symbol)
  ;  :group-items (maps in vector)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :parent
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :auto}
  ;
  ; @usage
  ; [vertical-group {...}]
  ;
  ; @usage
  ; [vertical-group :my-vertical-group {...}]
  ;
  ; @usage
  ; [vertical-group {:default-props {:hover-color :highlight}
  ;                  :element #'elements.api/button
  ;                  :group-items [{:label "First button"  :href "/first"}
  ;                                {:label "Second button" :href "/second"}]}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parametering)
   (fn [_ group-props]
       (let [group-props (pretty-presets/apply-preset                     group-props)
             group-props (vertical-group.prototypes/group-props-prototype group-props)]
            [vertical-group group-id group-props]))))
