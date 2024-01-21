
(ns pretty-elements.breadcrumbs.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [metamorphic-content.api                :as metamorphic-content]
              [pretty-elements.breadcrumbs.attributes :as breadcrumbs.attributes]
              [pretty-elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]
              [pretty-presets.api                     :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs-crumb
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb-props
  ; {:content (metamorphic-content)(opt)
  ;  :href (string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :placeholder (metamorphic-content)(opt)}}
  [breadcrumbs-id breadcrumbs-props {:keys [content href on-click-f placeholder] :as crumb-props}]
  [(cond href :a on-click-f :button :else :div)
   (breadcrumbs.attributes/crumb-attributes breadcrumbs-id breadcrumbs-props crumb-props)
   [metamorphic-content/compose content placeholder]])

(defn- breadcrumbs-crumb-list
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:crumbs (maps in vector)}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  ; A separator DIV is placed between crumbs instead of applying a CSS gap.
  ; The separator DIVs contain pseudo elements which displays a small dot between each crumbs.
  ; If the crumbs contained those pseudo elements, they would be part of the crumbs and would be clickable.
  ; ... And we don't want clickable dots between crumbs, do we?
  (letfn [(f0 [dex crumb-props]
              (let [crumb-props (pretty-presets/apply-preset crumb-props)
                    crumb-props (breadcrumbs.prototypes/crumb-props-prototype breadcrumbs-id breadcrumbs-props crumb-props)]
                   [:<> (if (-> dex zero? not) [:div {:class :pe-breadcrumbs--separator}])
                        [breadcrumbs-crumb breadcrumbs-id breadcrumbs-props crumb-props]]))]
         (hiccup/put-with-indexed [:<>] crumbs f0)))

(defn- breadcrumbs
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [breadcrumbs-id breadcrumbs-props]
  [:div (breadcrumbs.attributes/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)
        [:div (breadcrumbs.attributes/breadcrumbs-body-attributes breadcrumbs-id breadcrumbs-props)
              [breadcrumbs-crumb-list                             breadcrumbs-id breadcrumbs-props]]])

(defn element
  ; @preview (pretty-elements/breadcrumbs.png)
  ;
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :crumbs (maps in vector)
  ;   [{:click-effect (keyword)(opt)
  ;      Default: :opacity (if 'href' or 'on-click-f' is provided)
  ;     :content (metamorphic-content)(opt)
  ;     :disabled? (boolean)(opt)
  ;     :href (string)(opt)
  ;     :hover-effect (keyword)(opt)
  ;     :on-click-f (function or Re-Frame metamorphic-event)(opt)
  ;     :placeholder (metamorphic-content)(opt)
  ;     :preset (keyword)(opt)}]
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [breadcrumbs {...}]
  ;
  ; @usage
  ; [breadcrumbs :my-breadcrumbs {...}]
  ([breadcrumbs-props]
   [element (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   ; @note (tutorials#parametering)
   (fn [_ breadcrumbs-props]
       (let [breadcrumbs-props (pretty-presets/apply-preset breadcrumbs-props)]
             ; breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)
            [breadcrumbs breadcrumbs-id breadcrumbs-props]))))
