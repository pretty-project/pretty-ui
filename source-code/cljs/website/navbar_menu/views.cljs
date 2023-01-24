
(ns website.navbar-menu.views
    (:require [elements.api                   :as elements]
              [random.api                     :as random]
              [website.navbar-menu.prototypes :as navbar-menu.prototypes]
              [website.sidebar.views          :as sidebar.views]
              [window-observer.api            :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-menu
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  [:div "xx"])

(defn- navbar-menu
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  [menu-id {:keys [threshold] :as menu-props}]
  (if (window-observer/viewport-width-min? threshold)
    [:div {:style {:display :flex}}
       [elements/dropdown-menu menu-id menu-props]
       [sidebar.views/component menu-id {:content [sidebar-menu menu-id menu-props]}]]))

(defn component
  ; XXX#0715 (source-code/cljs/elements/dropdown_menu/views.cljs)
  ; The navbar-menu component is based on the dropdown-menu element.
  ; For more information check out the documentation of the dropdown-menu element.
  ;
  ; @description
  ; This component implements the dropdown-menu element and in case of the viewport
  ; width is smaller than the given threshold, it displays the menu items on
  ; a sidebar and replaces the menu bar with a single menu button.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {
  ;  :threshold (px)(opt)}
  ;
  ; @usage
  ; [navbar-menu {...}]
  ;
  ; @usage
  ; [navbar-menu :my-navbar-menu {...}]
  ([menu-props]
   [component (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   (let [menu-props (navbar-menu.prototypes/menu-props-prototype menu-props)]
        [navbar-menu menu-id menu-props])))
