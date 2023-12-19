
(ns pretty-elements.horizontal-group.views
    (:require [fruits.hiccup.api                           :as hiccup]
              [fruits.random.api                           :as random]
              [pretty-elements.horizontal-group.attributes :as horizontal-group.attributes]
              [pretty-elements.horizontal-group.prototypes :as horizontal-group.prototypes]
              [pretty-presets.api                          :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id {:keys [default-props element group-items] :as group-props}]
  [:div (horizontal-group.attributes/group-attributes group-id group-props)
        [:div (horizontal-group.attributes/group-body-attributes group-id group-props)
              (letfn [(f0 [group-item] [element (merge default-props group-item)])]
                     (hiccup/put-with [:<>] group-items f0))]])

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :default-props (map)(opt)
  ;  :element (Reagent component symbol)
  ;  :group-items (maps in vector)
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
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
  ;
  ; @usage
  ; [horizontal-group {...}]
  ;
  ; @usage
  ; [horizontal-group :my-horizontal-group {...}]
  ;
  ; @usage
  ; [horizontal-group {:default-props {:hover-color :highlight}
  ;                    :element #'elements.api/button
  ;                    :group-items [{:label "First button"  :href "/first"}
  ;                                  {:label "Second button" :href "/second"}]}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (fn [_ group-props] ; XXX#0106 (tutorials.api#parametering)
       (let [group-props (pretty-presets/apply-preset                       group-props)
             group-props (horizontal-group.prototypes/group-props-prototype group-props)]
            [horizontal-group group-id group-props]))))
