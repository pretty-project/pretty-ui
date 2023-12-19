
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
  ; {:href (string)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [breadcrumbs-id breadcrumbs-props {:keys [href label on-click placeholder] :as crumb-props}]
  [(cond href :a on-click :button :else :div)
   (breadcrumbs.attributes/crumb-attributes breadcrumbs-id breadcrumbs-props crumb-props)
   (metamorphic-content/compose label placeholder)])

(defn- breadcrumbs-crumb-list
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:crumbs (maps in vector)}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  ; A separator DIV placed between crumbs instead of applying CSS gap.
  ; The separator DIVs contain pseudo elements which displays a small dot between each crumbs.
  ; In case the crumbs contain those pseudo elements they would be part
  ; of the crumbs and would be clickable.
  ; ... And we don't want clickable dots between crumbs, do we?
  (letfn [(f0 [dex crumb-props]
              (let [crumb-props (pretty-presets/apply-preset                  crumb-props)
                    crumb-props (breadcrumbs.prototypes/crumb-props-prototype crumb-props)]
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
  ;   [{:href (string)(opt)
  ;     :label (metamorphic-content)(opt)
  ;     :on-click (Re-Frame metamorphic-event)(opt)
  ;     :placeholder (metamorphic-content)(opt)
  ;     :preset (keyword)(opt)}]
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  ;   Default: :s
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
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [breadcrumbs {...}]
  ;
  ; @usage
  ; [breadcrumbs :my-breadcrumbs {...}]
  ;
  ; @preview (pretty-elements/breadcrumbs-2.png)
  ; [breadcrumbs {:crumbs [...]}]
  ([breadcrumbs-props]
   [element (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   (fn [_ breadcrumbs-props] ; XXX#0106 (tutorials.api#parametering)
       (let [breadcrumbs-props (pretty-presets/apply-preset                        breadcrumbs-props)
             breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)]
            [breadcrumbs breadcrumbs-id breadcrumbs-props]))))
