
(ns components.list-item-avatar.views
    (:require [components.user-avatar.views :as user-avatar.views]
              [random.api                   :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-avatar
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  [avatar-id avatar-props]
  [:div.c-list-item-avatar [user-avatar.views/component avatar-id avatar-props]])

(defn component
  ; @param (keyword)(opt) avatar-id
  ; @param (map) avatar-props
  ; {:colors (strings in vector)(opt)
  ;  :first-name (string)(opt)
  ;  :last-name (string)(opt)
  ;  :size (px)(opt)
  ;   Default: 60}
  ;
  ; @usage
  ; [list-item-avatar {...}]
  ;
  ; @usage
  ; [list-item-avatar :my-avatar {...}]
  ([avatar-props]
   [component (random/generate-keyword) avatar-props])

  ([avatar-id avatar-props]
   [list-item-avatar avatar-id avatar-props]))
