
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
  ; XXX#0720 (source-code/cljs/components/user_avatar/views.cljs)
  ; The list-item-avatar component is based on the user-avatar component.
  ; For more information check out the documentation of the user-avatar component.
  ;
  ; @param (keyword)(opt) avatar-id
  ; @param (map) avatar-props
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
