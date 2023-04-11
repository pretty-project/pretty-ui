
(ns website.menu.views
    (:require [dom.api                 :as dom]
              [elements.api            :as elements]
              [metamorphic-content.api :as metamorphic-content]
              [random.api              :as random]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:menu-link (namespaced map)
  ;   {:menu/id (string)}}
  [_ {{:menu/keys [id]} :menu-link}]
  (let [menu-items @(r/subscribe [:website-menus.handler/get-menu-items id])]
       (letfn [(f [menu-items {:keys [link label style target]}]
                  (conj menu-items [:a {:data-click-effect :opacity
                                        :class [:mt-menu--menu-item :mt-effect--underline]
                                        :style style :href link :target (case target :self "_self" :blank "_blank" "_self")
                                        :on-mouse-up #(dom/blur-active-element!)}
                                       (metamorphic-content/compose label)]))]
              [:div {:class :mt-menu--menu-items}
                    (reduce f [:<>] menu-items)])))

(defn- menu
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :style (map)(opt)}
  [component-id {:keys [class style] :as component-props}]
  [menu-items component-id component-props])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :menu-link (namespaced map)
  ;   {:menu/id (string)}
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [menu {...}]
  ;
  ; @usage
  ; [menu :my-menu {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [menu component-id component-props]))
