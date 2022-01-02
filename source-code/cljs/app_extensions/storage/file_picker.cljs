
(ns app-extensions.storage.file-picker
    (:require [x.app-core.api :as a]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-file-picker-props
  [db [_ picker-id]]
  {:show-order-by-options? (get-in db [:storage :file-picker/meta-items :show-order-by-options?])})

(a/reg-sub :storage/get-file-picker-props get-file-picker-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-file-picker-order-by-options!
  [db [_ picker-id]]
  (update-in db [:storage :file-picker/meta-items :show-order-by-options?] not))

(a/reg-event-db :storage/toggle-file-picker-order-by-options! toggle-file-picker-order-by-options!)



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- download-more-items-button
  [picker-id]
  [:button {:style {:font-weight "600" :font-size "var(--font-size-xs)" :height "48px"
                    :width "100%" :display "flex" :justify-content "center" :align-items "center"
                    :color "var(--color-secondary)" }}
           (str "Download more items")])

(defn- file-picker-directory
  [picker-id name size modified-at]
  [:button {:style {:height "48px" :display "flex" :justify-content "space-between" :padding "0 12px"
                    :width "100%"}
            :class "xxx"}
          [:div {:style {:display "flex" :align-items "center" :height "100%" :font-size "var(--font-size-xs)"
                         :font-weight "500"}}
                (str name)]
          [:div {:style {:display "flex" :height "100%"}}
                [:div {:style {:display "flex" :align-items "center" :height "100%" :font-size "var(--font-size-xs)"
                               :font-weight "500"}}
                      (str size "MB")]
                [:div {:style {:display "flex" :align-items "center" :height "100%" :font-size "var(--font-size-xs)"
                               :justify-content "right" :font-weight "500" :width "50px"}}
                      (str modified-at)]]])

(defn- file-picker-item-list
  [picker-id])

(defn- file-picker-body
  [picker-id picker-props]
  [:div {:style {:background "var(--fill-color)"}}
        [:style {:type "text/css"} ".xxx:hover {background: var(--background-color-highlight)}"]
        [file-picker-directory picker-id "Red directory" "12.0" "3m"]
        [file-picker-directory picker-id "Blue directory" "2.0" "6m"]
        [file-picker-directory picker-id "Pink directory" "2.0" "1d"]
        [file-picker-directory picker-id "Grey directory" "2.0" "3d"]
        [file-picker-directory picker-id "Blue directory" "2.0" "2M"]
        [file-picker-directory picker-id "Brown directory" "2.0" "6M"]
        [file-picker-directory picker-id "White directory" "2.0" "6M"]
        [file-picker-directory picker-id "Black directory" "2.0" "6M"]
        [file-picker-directory picker-id "Yellow directory" "2.0" "6M"]
        [file-picker-directory picker-id "Orange directory" "2.0" "6M"]
        [file-picker-directory picker-id "Crimson directory" "2.0" "6M"]
        [file-picker-directory picker-id "Tourquise directory" "2.0" "6M"]
        [download-more-items-button picker-id]
        (if (get picker-props :show-order-by-options?)
            [:div {:style {:display "flex" :flex-direction "column" :justify-content "center" :align-items "center"
                           :width "100%" :height "100%" :background "var(--fill-color)" :top 0 :left 0
                           :position "absolute"}}
                  [:div {:style {:font-size "var(--font-size-s)" :font-weight "500"}}
                   "By name (ascending)"]
                  [:div {:style {:font-size "var(--font-size-s)" :font-weight "500"}}
                   "By name (descending)"]])])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-picker-header
  [picker-id picker-props]
  [:div {:style {:background "var(--fill-color)" :border-bottom "1px solid var(--border-color-highlight)"}}
        [:div {:style {:display "flex"}}
              [x.app-elements.api/button {:preset :add-icon-button}]
              [x.app-elements.api/button {:preset :select-mode-icon-button}]
              [x.app-elements.api/button {:preset :order-by-icon-button :on-click [:storage/toggle-file-picker-order-by-options! picker-id]}]]
        [:div {:style {:height "48px" :display "none" :align-items "center"
                       :justify-content "space-between" :padding "0 12px"}}
              [:button {:style {:display "flex"}}
                       [:div {:style {:display "flex" :align-items "center" :justify-content "center" :height "48px"
                                      :padding-right "12px" :font-size "var(--font-size-xs)" :font-weight 500}}
                             "Name"]
                       [:div {:style {:display "flex" :align-items "center" :justify-content "center" :height "48px"}}
                             [x.app-elements.api/icon {:icon :arrow_downward :size :xs}]]]
             [:div {:style {:display "flex"}}
                   [:button {:style {:display "flex"}}
                            [:div {:style {:display "none" :align-items "center" :justify-content "right" :height "48px"
                                           :padding-right "0" :font-size "var(--font-size-xs)" :font-weight 500}}
                                  "Size"]
                            [:div {:style {:display "flex" :align-items "center" :justify-content "right" :height "48px"}}
                                  [x.app-elements.api/icon {:icon :arrow_upward :size :xs :color :highlight}]]]
                  [:button {:style {:width "50px"}}
                           [:div {:style {:display "none" :align-items "center" :justify-content "right" :height "48px"
                                          :font-size "var(--font-size-xs)" :font-weight 500}}

                                 "Modified at"]
                           [:div {:style {:display "flex" :align-items "center" :justify-content "right" :height "48px"}}
                                 [x.app-elements.api/icon {:icon :arrow_upward :size :xs :color :highlight}]]]]]])



;; -- Layout components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-picker
  [picker-id picker-props]
  [:div {:style {:margin "48px" :min-width "560px" :border-radius "var(--border-radius-m) var(--border-radius-m)"
                 :overflow "hidden"}}
        [file-picker-header picker-id picker-props]
        [file-picker-body   picker-id picker-props]])

(defn view
  ([picker-props]
   [view (a/id) picker-props])

  ([picker-id picker-props]
   (let [subscribed-props (a/subscribe [:storage/get-file-picker-props picker-id])]
        (fn [] [file-picker picker-id (merge picker-props @subscribed-props)]))))
