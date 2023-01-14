
(ns templates.static-page.side-menu.views
    (:require [components.api   :as components]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- side-menu-structure
  ; @param (keyword) page-id
  ; @param (map) menu-props
  [page-id {:keys [content threshold]}]
  [components/side-menu ::side-menu
                        {:content [:<> [components/side-menu-header {}]
                                       [x.components/content page-id content]
                                       [components/side-menu-footer {}]]
                         :indent    {:left :xs}
                         :min-width :m
                         :threshold threshold}])

(defn side-menu
  ; @param (keyword) page-id
  ; @param (map) menu-props
  ; {:content (metamorphic-content)
  ;  :threshold (px)(opt)}
  ;
  ; @usage
  ; [side-menu :my-static-page {...}]
  [page-id menu-props]
  [x.components/delayer ::menu
                        {:content     [side-menu-structure page-id menu-props]
                         :placeholder [side-menu-structure page-id menu-props]
                         :timeout     500}])
