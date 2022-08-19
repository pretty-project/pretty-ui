

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.api
    (:require [dom.attribute             :as attribute]
              [dom.body                  :as body]
              [dom.class                 :as class]
              [dom.dimensions            :as dimensions]
              [dom.document              :as document]
              [dom.element               :as element]
              [dom.event                 :as event]
              [dom.file-selector         :as file-selector]
              [dom.file                  :as file]
              [dom.focus                 :as focus]
              [dom.form-data             :as form-data]
              [dom.head                  :as head]
              [dom.intersection-observer :as intersection-observer]
              [dom.listeners             :as listeners]
              [dom.mouse                 :as mouse]
              [dom.node                  :as node]
              [dom.script                :as script]
              [dom.scroll                :as scroll]
              [dom.selection             :as selection]
              [dom.style                 :as style]
              [dom.touch                 :as touch]
              [dom.units                 :as units]
              [dom.viewport              :as viewport]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; dom.attribute
(def get-element-attribute     attribute/get-element-attribute)
(def set-element-attribute!    attribute/set-element-attribute!)
(def remove-element-attribute! attribute/remove-element-attribute!)

; dom.body
(def get-body-element     body/get-body-element)
(def get-body-style-value body/get-body-style-value)

; dom.class
(def set-element-class!    class/set-element-class!)
(def remove-element-class! class/remove-element-class!)

; dom.dimensions
(def get-element-width                 dimensions/get-element-width)
(def get-element-height                dimensions/get-element-height)
(def get-element-relative-left         dimensions/get-element-relative-left)
(def get-element-relative-top          dimensions/get-element-relative-top)
(def get-element-absolute-left         dimensions/get-element-absolute-left)
(def get-element-absolute-top          dimensions/get-element-absolute-top)
(def get-element-offset-left           dimensions/get-element-offset-left)
(def get-element-offset-top            dimensions/get-element-offset-top)
(def get-element-masspoint-x           dimensions/get-element-masspoint-x)
(def get-element-masspoint-y           dimensions/get-element-masspoint-y)
(def element-on-viewport-left?         dimensions/element-on-viewport-left?)
(def element-on-viewport-right?        dimensions/element-on-viewport-right?)
(def element-on-viewport-top?          dimensions/element-on-viewport-top?)
(def element-on-viewport-bottom?       dimensions/element-on-viewport-bottom?)
(def get-element-masspoint-orientation dimensions/get-element-masspoint-orientation)

; dom.document
(def get-document-element document/get-document-element)
(def get-document-height  document/get-document-height)
(def get-document-width   document/get-document-width)

; dom.element
(def get-element-by-id              element/get-element-by-id)
(def get-elements-by-query-selector element/get-elements-by-query-selector)
(def get-elements-by-class-name     element/get-elements-by-class-name)
(def get-elements-by-tag-name       element/get-elements-by-tag-name)
(def element-disabled?              element/element-disabled?)
(def element-enabled?               element/element-enabled?)
(def set-element-id!                element/set-element-id!)

; dom.event
(def on-mouse-left     event/on-mouse-left)
(def stop-propagation! event/stop-propagation!)
(def prevent-default!  event/prevent-default!)
(def event->node-name  event/event->node-name)
(def event->value      event/event->value)

; dom.file-selector
(def file-selector->files              file-selector/file-selector->files)
(def file-selector->file-list          file-selector/file-selector->file-list)
(def file-selector->file               file-selector/file-selector->file)
(def file-selector->files-size         file-selector/file-selector->files-size)
(def file-selector->file-count         file-selector/file-selector->file-count)
(def file-selector->any-file-selected? file-selector/file-selector->any-file-selected?)
(def file-selector->mime-types         file-selector/file-selector->mime-types)
(def file-selector->files-data         file-selector/file-selector->files-data)
(def file-selector->files-meta         file-selector/file-selector->files-meta)
(def file-selector->image-data-url     file-selector/file-selector->image-data-url)

; dom.file
(def file->filesize  file/file->filesize)
(def file->filename  file/file->filename)
(def file->mime-type file/file->mime-type)
(def file->image?    file/file->image?)
(def file->file-data file/file->file-data)

; dom.focus
(def get-active-element focus/get-active-element)
(def focus-element!     focus/focus-element!)
(def blur-element!      focus/blur-element!)

; dom.form-data
(def append-to-form-data!     form-data/append-to-form-data!)
(def merge-to-form-data!      form-data/merge-to-form-data!)
(def file-selector->form-data form-data/file-selector->form-data)

; dom.head
(def get-head-element head/get-head-element)

; dom.intersection-observer
(def intersection-observer         intersection-observer/intersection-observer)
(def setup-intersection-observer!  intersection-observer/setup-intersection-observer!)
(def remove-intersection-observer! intersection-observer/remove-intersection-observer!)

; dom.listeners
(def add-event-listener!    listeners/add-event-listener!)
(def remove-event-listener! listeners/remove-event-listener!)

; dom.mouse
(def get-mouse-x                mouse/get-mouse-x)
(def get-mouse-y                mouse/get-mouse-y)
(def get-mouse-viewport-quarter mouse/get-mouse-viewport-quarter)
(def select-preventer           mouse/select-preventer)

; dom.node
(def insert-before!                     node/insert-before!)
(def insert-after!                      node/insert-after!)
(def insert-as-first-of-type!           node/insert-as-first-of-type!)
(def insert-as-last-of-type!            node/insert-as-last-of-type!)
(def insert-as-first-of-query-selected! node/insert-as-first-of-query-selected!)
(def insert-as-last-of-query-selected!  node/insert-as-last-of-query-selected!)
(def append-element!                    node/append-element!)
(def prepend-element!                   node/prepend-element!)
(def create-element!                    node/create-element!)
(def remove-element!                    node/remove-element!)
(def remove-child!                      node/remove-child!)
(def empty-element!                     node/empty-element!)
(def set-element-content!               node/set-element-content!)

; dom.script
(def append-script! script/append-script!)

; dom.scroll
(def get-scroll-x           scroll/get-scroll-x)
(def get-scroll-y           scroll/get-scroll-y)
(def scroll-direction-ttb?  scroll/scroll-direction-ttb?)
(def scroll-direction-btt?  scroll/scroll-direction-btt?)
(def get-scroll-direction   scroll/get-scroll-direction)
(def get-scroll-progress    scroll/get-scroll-progress)
(def set-scroll-x!          scroll/set-scroll-x!)
(def set-scroll-y!          scroll/set-scroll-y!)
(def scroll-to-element-top! scroll/scroll-to-element-top!)

; dom.selection
(def get-selection-start  selection/get-selection-start)
(def get-selection-end    selection/get-selection-end)
(def set-selection-start! selection/set-selection-start!)
(def set-selection-end!   selection/set-selection-end!)
(def set-selection-range! selection/set-selection-range!)
(def set-caret-position!  selection/set-caret-position!)

; dom.style
(def get-element-style           style/get-element-style)
(def set-element-style!          style/set-element-style!)
(def remove-element-style!       style/remove-element-style!)
(def get-element-style-value     style/get-element-style-value)
(def set-element-style-value!    style/set-element-style-value!)
(def remove-element-style-value! style/remove-element-style-value!)
(def get-element-computed-style  style/get-element-computed-style)

; dom.touch
(def touch-events-api-detected? touch/touch-events-api-detected?)

; dom.units
(def px->vh units/px->vh)
(def px->vw units/px->vw)
(def vh->px units/vh->px)
(def vw->px units/vw->px)

; dom.viewport
(def get-viewport-height      viewport/get-viewport-height)
(def get-viewport-width       viewport/get-viewport-width)
(def viewport-profile-match?  viewport/viewport-profile-match?)
(def viewport-profiles-match? viewport/viewport-profiles-match?)
(def get-viewport-profile     viewport/get-viewport-profile)
(def get-viewport-orientation viewport/get-viewport-orientation)
(def square-viewport?         viewport/square-viewport?)
(def landscape-viewport?      viewport/landscape-viewport?)
(def portrait-viewport?       viewport/portrait-viewport?)
