
(ns templates.item-handler.comments.views
    (:require [components.api           :as components]
              [elements.api             :as elements]
              [engines.item-handler.api :as item-handler]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- comment-item
  ; @param (keyword) handler-id
  ; @param (map) comments-props
  [handler-id _])

(defn view
  ; @param (keyword) handler-id
  ; @param (map) comments-props
  ; {}
  ;
  ; @usage
  ; [comments :my-handler {...}]
  [handler-id comments-props]
  (let []; comments-props (comments.prototypes/comments-props-prototype handler-id comments-props)
       (let [current-item-path @(r/subscribe [:item-handler/get-current-item-path handler-id])]
            [:div {:id :t-item-handler--comments}
                  [components/vector-items-header ::header {:label "Comments" :outdent {:top :5xl}}]
                  [components/vector-item-list ::client-contact-list
                                               {:item-element #'comment-item
                                                :on-change    [:item-handler/current-item-changed handler-id]
                                                :outdent      {:bottom :5xl}
                                                :placeholder  {:label "Leave the first comment" :illustration :comments}
                                                :value-path   (conj current-item-path :comments)}]])))
