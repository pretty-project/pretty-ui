
(ns elements.breadcrumbs.views
    (:require [elements.breadcrumbs.attributes :as breadcrumbs.attributes]
              [elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]
              [hiccup.api                      :as hiccup]
              [random.api                      :as random]
              [x.components.api                :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs-crumb
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ; {:href (string)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [breadcrumbs-id breadcrumbs-props {:keys [href label on-click placeholder] :as crumb}]
  [(cond href :a on-click :button :else :div)
   (breadcrumbs.attributes/crumb-attributes breadcrumbs-id breadcrumbs-props crumb)
   (if (-> label       x.components/content empty?)
       (-> placeholder x.components/content)
       (-> label       x.components/content))])

(defn- breadcrumbs-crumb-list
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:crumbs (maps in vector)}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  ; A separator DIV placed between crumbs instead of applying CSS gap because
  ; this way, the separator DIVs contain pseudo elements which displays a small
  ; dot between each crumbs.
  ; In case of the crumbs contain those pseudo elements they might be part
  ; of the crumbs and might be clickable. And we don't want clickable dots between crumbs.
  (letfn [(f [dex crumb-props]
             (let [crumb-props (breadcrumbs.prototypes/crumb-props-prototype crumb-props)]
                  [:<> (if (not= 0 dex) [:div {:class :e-breadcrumbs--separator}])
                       [breadcrumbs-crumb breadcrumbs-id breadcrumbs-props crumb-props]]))]
         (hiccup/put-with-indexed [:<>] crumbs f)))

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
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :crumbs (maps in vector)
  ;   [{:href (string)(opt)
  ;     :label (metamorphic-content)(opt)
  ;     :on-click (Re-Frame metamorphic-event)(opt)
  ;     :placeholder (metamorphic-content)(opt)}]
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
  ;   Same as the :indent property
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
   (let [breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)]
        [breadcrumbs breadcrumbs-id breadcrumbs-props])))
