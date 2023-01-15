
(ns templates.item-browser.header.views
    (:require [components.api                     :as components]
              [elements.api                       :as elements]
              [re-frame.api                       :as r]
              [templates.item-lister.header.views :as header.views]
              [x.components.api                   :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ghost-view
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ; {}
  [browser-id {:keys [crumbs] :as header-props}]
  [:div#t-item-lister--label-bar [components/ghost-view {:layout :box-surface-header :breadcrumb-count (count crumbs)}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compact-bar
  ; @param (keyword) browser-id
  ; @param (map) bar-props
  ; {:order-by-options (keywords or namespaced keywords in vector)}
  [browser-id bar-props])
  ;[header.views/compact-bar browser-id bar-props])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-info
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ; {:item-info (metamorphic-content)(opt)}
  [browser-id {:keys [item-info]}]
  (if item-info [components/section-description ::item-info
                                                {:content          item-info
                                                 :horizontal-align :right
                                                 :outdent          {:top :m :right :xs}}]))

(defn search-field
  ; @param (keyword) browser-id
  ; @param (map) header-props
  [browser-id header-props]
  [header.views/search-field browser-id header-props])

(defn search-description
  ; @param (keyword) browser-id
  ; @param (map) header-props
  [browser-id header-props]
  [header.views/search-description browser-id header-props])

(defn- control-bar
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ; {:controls (metamorphic-content)}
  [browser-id {:keys [controls] :as header-props}]
  (if controls [:div#t-item-lister--control-bar [x.components/content browser-id controls]]))

(defn- breadcrumbs
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ; {:crumbs (maps in vector)}
  [browser-id {:keys [crumbs]}]
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? browser-id])]
       [elements/breadcrumbs ::breadcrumbs
                             {:crumbs    crumbs
                              :disabled? browser-disabled?}]))

(defn- title
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ; {:title (metamorphic-content)(opt)}
  [browser-id {:keys [title]}]
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled?      browser-id])
        item-label        @(r/subscribe [:item-browser/get-current-item-label browser-id])]
       [components/section-title ::title
                                 {:content   (or item-label title)
                                  :disabled? browser-disabled?}]))

(defn- label-bar
  ; @param (keyword) browser-id
  ; @param (map) header-props
  [browser-id header-props]
  [:div#t-item-lister--label-bar [:div.t-item-browser--lister--block [title       browser-id header-props]
                                                                     [breadcrumbs browser-id header-props]]])

(defn- header
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ; {:controls (metamorphic-content)(opt)
  ;  :crumbs (maps in vector)
  ;   [{:label (metamorphic-content)(opt)
  ;     :placeholder (metamorphic-content)
  ;     :route (string)(opt)}]
  ;  :placeholder (metamorphic-content)(opt)
  ;  :title (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [header :my-browser {...}]
  [browser-id header-props])
  ;(if-let [first-data-received? @(r/subscribe [:item-browser/first-data-received? browser-id])]
  ;        [:<> [:div#t-item-lister--header [label-bar   browser-id header-props]
  ;                                         [control-bar browser-id header-props]
  ;        [:<> [:div#t-item-lister--header [ghost-view  browser-id header-props]]]])

;  (if-let [first-data-received? @(r/subscribe [:item-browser/first-data-received? browser-id])]
;          [:<> [title        browser-id header-props]
;               [breadcrumbs  browser-id header-props]
;               [search-field browser-id header-props]
;               [:div {:style {:display "flex" :justify-content "space-between"}}
;                     [search-description browser-id header-props]
;                     [item-info          browser-id header-props]]]
;          [:<> [components/ghost-view {:layout :box-surface-header :breadcrumb-count 2}]]))
;               ;[components/ghost-view {:layout :box-surface-search-bar}]]))
