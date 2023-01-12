
(ns templates.static-page.header.views
    (:require [components.api   :as components]
              [elements.api     :as elements]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ghost-view
  ; @param (keyword) page-id
  ; @param (map) header-props
  ; {:crumbs (maps in vector)}
  [_ {:keys [crumbs disabled?]}]
  (if (-> crumbs empty? not)
      [:div#t-static-page--label-bar [components/ghost-view {:layout :box-surface-header :breadcrumb-count (count crumbs)}]]
      [:div#t-static-page--label-bar [components/ghost-view {:layout :box-surface-header}]]))

(defn- breadcrumbs
  ; @param (keyword) page-id
  ; @param (map) header-props
  ; {:crumbs (maps in vector)
  ;  :disabled? (boolean)(opt)}
  [_ {:keys [crumbs disabled?]}]
  (if (-> crumbs empty? not)
      [elements/breadcrumbs ::breadcrumbs
                            {:crumbs    crumbs
                             :disabled? disabled?}]))

(defn- title
  ; @param (keyword) page-id
  ; @param (map) header-props
  ; {:disabled? (boolean)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  [_ {:keys [disabled? placeholder title]}]
  [components/section-title ::title
                            {:content     title
                             :disabled?   disabled?
                             :placeholder placeholder}])

(defn- label-bar
  ; @param (keyword) page-id
  ; @param (map) header-props
  [page-id header-props]
  [:div#t-static-page--label-bar [title       page-id header-props]
                                 [breadcrumbs page-id header-props]])

(defn header
  ; @param (keyword) page-id
  ; @param (map) header-props
  ; {:crumbs (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :placeholder (metamorphic-content)(opt)
  ;     :route (string)(opt)}]
  ;  :disabled? (boolean)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [header :my-static-page {...}]
  [page-id {:keys [crumbs] :as header-props}]
  [x.components/delayer ::header
                        {:content     [:div#t-static-page--header [label-bar  page-id header-props]]
                         :placeholder [:div#t-static-page--header [ghost-view page-id header-props]]
                         :timeout     500}])
