
(ns components.consent-dialog.views
    (:require [components.consent-dialog.prototypes :as consent-dialog.prototypes]
              [fruits.random.api                    :as random]
              [metamorphic-content.api              :as metamorphic-content]
              [pretty-elements.api                  :as pretty-elements]
              [pretty-layouts.api                   :as pretty-layouts]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- consent-dialog-body
  ; @param (keyword) dialog-id
  ; @param (map) dialog-props
  ; {:content (metamorphic-content)}
  [dialog-id {:keys [content]}]
  [metamorphic-content/compose content])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- primary-button
  ; @param (keyword) dialog-id
  ; @param (map) dialog-props
  [_ _])

(defn- secondary-button
  ; @param (keyword) dialog-id
  ; @param (map) dialog-props
  [_ _])

(defn- header-label
  ; @param (keyword) dialog-id
  ; @param (map) dialog-props
  ; {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  [pretty-elements/label ::header-label
                         {:content label
                          :indent  {:horizontal :xs :left :s}}])

(defn- consent-dialog-header
  ; @param (keyword) dialog-id
  ; @param (map) dialog-props
  [dialog-id dialog-props]
  [pretty-elements/horizontal-polarity ::consent-dialog-header
                                       {:start-content  [secondary-button dialog-id dialog-props]
                                        :middle-content [header-label     dialog-id dialog-props]
                                        :end-content    [primary-button   dialog-id dialog-props]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- consent-dialog
  ; @param (keyword) dialog-id
  ; @param (map) dialog-props
  [dialog-id dialog-props]
  [pretty-layouts/struct-popup :components.consent-dialog/view
                               {:body   [consent-dialog-body   dialog-id dialog-props]
                                :header [consent-dialog-header dialog-id dialog-props]
                                :min-width :xs}])

(defn component
  ; @param (keyword)(opt) dialog-id
  ; @param (map) dialog-props
  ; {:content (metamorphic-content)
  ;  :label (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [consent-dialog {...}]
  ;
  ; @usage
  ; [consent-dialog :my-consent-dialog {...}]
  ([dialog-props]
   [component (random/generate-keyword) dialog-props])

  ([dialog-id dialog-props]
   (fn [_ dialog-props] ; XXX#0106 (README.md#parametering)
       (let [] ; dialog-props (consent-dialog.prototypes/dialog-props-prototype dialog-props)
            [consent-dialog dialog-id dialog-props]))))
