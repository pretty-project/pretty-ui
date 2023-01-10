
(ns website.menu.views
    (:require [elements.api      :as elements]
              [random.api        :as random]
              [re-frame.api      :as r]
              [x.components.api  :as x.components]
              [x.environment.api :as x.environment]))

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
                  (conj menu-items [:a {:data-click-effect :opacity}
                                        :class [:mt-menu--menu-item :mt-effect--underline]
                                        :style style :href link :target (case target :self "_self" :blank "_blank" "_self")
                                        :on-mouse-up #(x.environment/blur-element!)
                                       (x.components/content label)]))]
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
