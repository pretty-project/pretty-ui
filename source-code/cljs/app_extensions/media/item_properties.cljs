
(ns app-extensions.media.item-properties
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.io      :as io]
              [mid-fruits.time    :as time]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-ui.api       :as ui]
              [app-extensions.media.engine :as engine]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- directory-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-props
  ;  {:directory/alias (string)}
  ;
  ; @return (component)
  [_ {:directory/keys [alias]}]
  [:<> [elements/label {:content :name :layout :fit :color :muted}]
       [elements/label {:content alias :layout :fit}]])

(defn- directory-modified-at
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-props
  ;  {:directory/modified-at (B)(opt)}
  ;
  ; @return (component)
  [_ {:directory/keys [modified-at]}]
  (if modified-at (let [modified-at (time/timestamp-string->date-time modified-at :yyyymmdd :hhmm)]
                       [:<> [elements/horizontal-line {:color :highlight :layout :row}]
                            [elements/label {:content :last-modified :layout :fit :color :muted}]
                            [elements/label {:content modified-at :layout :fit :selectable? true}]])))

(defn- directory-content-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-props
  ;  {:directory/content-size (B)(opt)}
  ;
  ; @return (component)
  [_ {:directory/keys [content-size]}]
  (if content-size (let [content-size (io/B->MB content-size)]
                        [:<> [elements/horizontal-line {:color :highlight :layout :row}]
                             [elements/label {:content :content-size :layout :fit :color :muted}]
                             [elements/label {:content (str content-size " MB") :layout :fit}]])))

(defn- directory-properties
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-props
  ;  {:directory/alias (metamorphic-content)
  ;   :directory/created-at (string)(opt)
  ;   :directory/description (string)(opt)
  ;   :directory/item-count (integer)(opt)
  ;   :directory/modified-at (string)(opt)}
  ;
  ; @return (component)
  [directory-id directory-props]
  [:<> [directory-name         directory-id directory-props]
      ;[directory-description  directory-id directory-props]])
      ;[directory-item-count   directory-id directory-props]])
       [directory-content-size directory-id directory-props]
      ;[directory-tags         directory-id directory-props]])
       [directory-modified-at  directory-id directory-props]
       [elements/horizontal-separator {:size :s}]])

(defn- file-modified-at
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:file/modified-at (B)(opt)}
  ;
  ; @return (component)
  [_ {:file/keys [modified-at]}]
  (if modified-at (let [modified-at (time/timestamp-string->date-time modified-at :yyyymmdd :hhmm)]
                       [:<> [elements/horizontal-line {:color :highlight :layout :row}]
                            [elements/label {:content :last-modified :layout :fit :color :muted}]
                            [elements/label {:content modified-at :layout :fit :selectable? true}]])))

(defn- file-filesize
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:file/filesize (B)(opt)}
  ;
  ; @return (component)
  [_ {:file/keys [filesize]}]
  (if filesize (let [filesize (io/B->MB filesize)]
                    [:<> [elements/horizontal-line {:color :highlight :layout :row}]
                         [elements/label {:content :filesize :layout :fit :color :muted}]
                         [elements/label {:content (str filesize " MB") :layout :fit}]])))

(defn- file-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:file/description (string)(opt)}
  ;
  ; @return (component)
  [_ {:file/keys [description]}]
  (if description [:<> [elements/horizontal-line {:color :highlight :layout :row}]
                       [elements/label {:content :description :layout :fit :color :muted}]
                       [elements/label {:content description  :layout :fit}]]))

(defn- file-filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:file/alias (string)}
  ;
  ; @return (component)
  [_ {:file/keys [alias]}]
  [:<> [elements/label {:content :filename :layout :fit :color :muted}]
       [elements/label {:content alias     :layout :fit :selectable? true}]])

(defn- file-properties
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:file/alias (metamorphic-content)
  ;   :file/description (string)(opt)
  ;   :file/filesize (B)(opt)
  ;   :file/modified-at (string)(opt)
  ;   :file/uploaded-at (string)(opt)}
  ;
  ; @return (component)
  [file-id file-props]
  [:<> [file-filename    file-id file-props]
      ;[file-description file-id file-props
       [file-filesize    file-id file-props]
      ;[file-tags        file-id file-props]
       [file-modified-at file-id file-props]
       [elements/horizontal-separator {:size :s}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :media/render-subdirectory-properties!
  ; @param (keyword) directory-id
  ; @param (keyword) subdirectory-id
  (fn [{:keys [db]} [_ directory-id subdirectory-id]]
      (let [subdirectory-props (r engine/get-subdirectory-props db directory-id subdirectory-id)]
           [:ui/add-popup! directory-id
                           {:content       #'directory-properties
                            :content-props subdirectory-props
                            :label-bar     {:content #'ui/close-popup-header}
                            :min-width     :xs}])))

(a/reg-event-fx
  :media/render-file-properties!
  ; @param (keyword) directory-id
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ directory-id file-id]]
      (let [file-props (r engine/get-file-props db directory-id file-id)]
           [:ui/add-popup! file-id
                           {:content       #'file-properties
                            :content-props file-props
                            :label-bar     {:content #'ui/close-popup-header}
                            :min-width     :xs}])))
