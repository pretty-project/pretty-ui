
(ns pretty-inputs.api
    (:require [pretty-inputs.checkbox.views        :as checkbox.views]
              [pretty-inputs.combo-box.views       :as combo-box.views]
              [pretty-inputs.counter.views         :as counter.views]
              [pretty-inputs.date-field.views      :as date-field.views]
              [pretty-inputs.digit-field.views     :as digit-field.views]
              [pretty-inputs.field.views           :as field.views]
              [pretty-inputs.header.views          :as header.views]
              [pretty-inputs.multi-combo-box.views :as multi-combo-box.views]
              [pretty-inputs.multi-field.views     :as multi-field.views]
              [pretty-inputs.multiline-field.views :as multiline-field.views]
              [pretty-inputs.number-field.views    :as number-field.views]
              [pretty-inputs.option-group.views    :as option-group.views]
              [pretty-inputs.option.views          :as option.views]
              [pretty-inputs.password-field.views  :as password-field.views]
              [pretty-inputs.radio-button.views    :as radio-button.views]
              [pretty-inputs.search-field.views    :as search-field.views]
              [pretty-inputs.select-button.views   :as select-button.views]
              [pretty-inputs.select.views          :as select.views]
              [pretty-inputs.slider.views          :as slider.views]
              [pretty-inputs.text-field.views      :as text-field.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def checkbox        checkbox.views/view)
(def combo-box       combo-box.views/view)
(def counter         counter.views/view)
(def date-field      date-field.views/view)
(def digit-field     digit-field.views/view)
(def field           field.views/view)
(def header          header.views/view)
(def multi-combo-box multi-combo-box.views/view)
(def multi-field     multi-field.views/view)
(def multiline-field multiline-field.views/view)
(def number-field    number-field.views/view)
(def option          option.views/view)
(def option-group    option-group.views/view)
(def password-field  password-field.views/view)
(def radio-button    radio-button.views/view)
(def search-field    search-field.views/view)
(def select          select.views/view)
(def select-button   select-button.views/view)
(def slider          slider.views/view)
(def text-field      text-field.views/view)
